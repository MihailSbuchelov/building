package ru.everybit.bzkpd_bsk.processes.manufacture.transfer_to_stock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.TransferItemsGrid;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItemDao;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItem;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItemDao;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockItem;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockItemDao;

import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PostingDetailsInStockForm extends BpmForm {
    private static final long serialVersionUID = 1604491585499884118L;
    
    private Long transferId;
    private TransferItemsGrid transferItemsGrid;
    final private BpmTransferToStockItemDao transferItemsDao;
    final private BpmStockItemDao stockItemDao;
    private BpmBuildingPlanItemDao buildingPlanItemDao;

    public PostingDetailsInStockForm() {
        super(false);
        transferItemsDao = BpmDaoFactory.getTransferToStockItemDao();
        stockItemDao = BpmDaoFactory.getStockItemDao();
        buildingPlanItemDao = BpmDaoFactory.getBuildingPlanItemDao();
    }

    @Override
    public void initUiContent() {
        transferId = (Long)getProcessVariable(BpmManufactureProcessModel.MANUFACTURE_TRANSFER_TO_STOCK_ID);
        transferItemsGrid = new TransferItemsGrid(){
            private static final long serialVersionUID = 1L;
            @Override
            public void loadData(List<BpmTransferToStockItem> transferItems) {
                super.loadData(transferItems);
                for (BpmTransferToStockItem transferItem : transferItems) {
                    setItemProperty(transferItem, "approvedQuantity", transferItem.getApprovedQuantity());
                }
            }
        };
        
        transferItemsGrid.addGridColumn("approvedQuantity", Integer.class, "Принято шт.");
        transferItemsGrid.addFooterSum("approvedQuantity");
        
        formContentLayout.addComponent(transferItemsGrid);
        
        transferItemsGrid.addSelectionListener(new SelectionListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void select(SelectionEvent event) {
                BpmTransferToStockItem transferItem = (BpmTransferToStockItem)transferItemsGrid.getSelectedRow();
                if(transferItem == null)
                    return;
                ApproveTransferEditor editor = new ApproveTransferEditor(transferItem);
                editor.show();
            }
        });
        reloadData();
    }

    public void reloadData(){
        List<BpmTransferToStockItem> transferItems = transferItemsDao.findByTransferToStockIdOrderByIdAsc(transferId);
        transferItemsGrid.loadData(transferItems);
    }

    @Override
    protected void submitButtonClick() {
        Collection<?> itemIds = transferItemsGrid.getContainerDataSource().getItemIds();
        List<BpmStockItem> savedItems = new ArrayList<BpmStockItem>();
        List<BpmBuildingPlanItem> savedBuildingPlanItems = new ArrayList<BpmBuildingPlanItem>();
        for (Object itemId : itemIds) {
            BpmTransferToStockItem transferItem = (BpmTransferToStockItem)itemId;
            if(transferItem.getQuantity() != transferItem.getApprovedQuantity()){
                Notification notification = new Notification("Не все детали оприходованы", Notification.Type.ERROR_MESSAGE);
                notification.setDelayMsec(2000);
                notification.setPosition(Position.BOTTOM_RIGHT);
                notification.show(Page.getCurrent());
                return;
            }
            BpmBuildingPlanItem buildingPlanItem = transferItem.getBuildingPlanItem();
            buildingPlanItem.setStockQuantity(buildingPlanItem.getStockQuantity() + transferItem.getApprovedQuantity());
            savedBuildingPlanItems.add(buildingPlanItem);
            
            BpmStockItem stockItem = stockItemDao.findByBuildingPlanItem(buildingPlanItem);
            if (stockItem == null) {
                stockItem = new BpmStockItem();
                stockItem.setBuildingPlanItem(buildingPlanItem);
                stockItem.setQuantity(transferItem.getApprovedQuantity());
            } else {
                stockItem.setQuantity(stockItem.getQuantity() + transferItem.getApprovedQuantity());
            }
            savedItems.add(stockItem);
        }

        stockItemDao.save(savedItems);
        buildingPlanItemDao.save(savedBuildingPlanItems);
        
        super.submitButtonClick();
    }

    class ApproveTransferEditor extends BpmWindow{
        private static final long serialVersionUID = 1L;
        public ApproveTransferEditor(final BpmTransferToStockItem transferItem) {
            
            super("Принять изделие");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            TextField articleTypeNameNameTextField = new TextField("Тип изделия");
            articleTypeNameNameTextField.setWidth("100%");
            BpmArticle article = transferItem.getManufactureDayPlanLineBuildingObjectItem().getArticle();
            articleTypeNameNameTextField.setValue(article.getArticleType().getName());
            articleTypeNameNameTextField.setReadOnly(true);
            layout.addComponent(articleTypeNameNameTextField);

            TextField articleNameTextField = new TextField("Наименование изделия");
            articleNameTextField.setWidth("100%");
            articleNameTextField.setValue(article.getName());
            articleNameTextField.setReadOnly(true);
            layout.addComponent(articleNameTextField);

            final ComboBox quantityComboBox = new ComboBox("Количество");
            quantityComboBox.setWidth("100%");
            quantityComboBox.setNullSelectionAllowed(false);
            quantityComboBox.setTextInputAllowed(false);
            int i;
            for (i = 0; i <= (int) transferItem.getQuantity(); i++) {
                quantityComboBox.addItem(i);
            }
            quantityComboBox.select(i - 1);
            layout.addComponent(quantityComboBox);

            Button editButton = new Button("Принять");
            editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    transferItem.setApprovedQuantity((int)quantityComboBox.getValue());
                    transferItemsDao.save(transferItem);
                    reloadData();
                    close();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }
    }
}
