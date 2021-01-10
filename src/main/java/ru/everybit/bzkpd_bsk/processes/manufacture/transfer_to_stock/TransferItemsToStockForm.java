package ru.everybit.bzkpd_bsk.processes.manufacture.transfer_to_stock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.ManufactureDayPlanLineBuildingObjectItemGrid;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.TransferItemsGrid;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItemDao;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItemDao;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStock;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockDao;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockItem;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockItemDao;

import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TransferItemsToStockForm extends BpmForm {
    private static final long serialVersionUID = 6895916487430287715L;
    private ManufactureDayPlanLineBuildingObjectItemGrid buildingObjectItemsGrid;
    private TransferItemsGrid transferItemsGrid;
    private BpmTransferToStock transfer;
    // DAO
    private BpmTransferToStockItemDao transferItemsDao;
    private BpmManufactureDayPlanLineBuildingObjectItemDao manufactureItemsDao;
    private BpmBuildingPlanItemDao buildingPlanItemDao;
    private BpmTransferToStockDao transferDao;

    public TransferItemsToStockForm() {
        super(false);
    }

    @Override
    public void initUiContent() {
        Long transferId = (Long)getProcessVariable(BpmManufactureProcessModel.MANUFACTURE_TRANSFER_TO_STOCK_ID);
        transferDao = BpmDaoFactory.getTransferToStockDao();
        manufactureItemsDao = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao();
        transferItemsDao = BpmDaoFactory.getTransferToStockItemDao();
        buildingPlanItemDao = BpmDaoFactory.getBuildingPlanItemDao();
        
        // Обновляем статус предачи
        transfer = transferDao.findOne(transferId);
        if(BpmTransferToStock.STATUS_NEW.equals(transfer.getStatus())){
            transfer.setStatus(BpmTransferToStock.STATUS_IN_PROGRESS);
            transferDao.save(transfer);
        }
        
        buildingObjectItemsGrid = new ManufactureDayPlanLineBuildingObjectItemGrid();
        formContentLayout.addComponent(buildingObjectItemsGrid);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        final Button addButton = new Button(FontAwesome.ARROW_DOWN);
        addButton.setEnabled(false);
        buttonLayout.addComponent(addButton);
        
        final Button removeButton = new Button(FontAwesome.ARROW_UP);
        removeButton.setEnabled(false);
        buttonLayout.addComponent(removeButton);
        formContentLayout.addComponent(buttonLayout);
        formContentLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
        
        transferItemsGrid = new TransferItemsGrid();
        formContentLayout.addComponent(transferItemsGrid);
        
        // Загружаем данные
        reloadData();
        
        // Настраиваем логику поведения компонентов
        buildingObjectItemsGrid.addSelectionListener(new SelectionListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void select(SelectionEvent event) {
                addButton.setEnabled(!event.getSelected().isEmpty());
            }
        });
        
        transferItemsGrid.addSelectionListener(new SelectionListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void select(SelectionEvent event) {
                removeButton.setEnabled(!event.getSelected().isEmpty());
            }
        });
        
        removeButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(ClickEvent event) {
                BpmTransferToStockItem transferItem = (BpmTransferToStockItem) transferItemsGrid.getSelectedRow();
                BpmBuildingPlanItem buildingPlanItem = transferItem.getBuildingPlanItem();
                BpmManufactureDayPlanLineBuildingObjectItem objectItem = transferItem.getManufactureDayPlanLineBuildingObjectItem();
                objectItem.setStockQuantity(objectItem.getStockQuantity() - transferItem.getQuantity());
                buildingPlanItem.setProductionQuantity(buildingPlanItem.getProductionQuantity() - transferItem.getQuantity());
                manufactureItemsDao.save(objectItem);
                transferItemsDao.delete(transferItem);
                buildingPlanItemDao.save(buildingPlanItem);
                reloadData();
            }
        });
        
        addButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(ClickEvent event) {
                BpmManufactureDayPlanLineBuildingObjectItem item = (BpmManufactureDayPlanLineBuildingObjectItem)buildingObjectItemsGrid.getSelectedRow();
                // На всякий случай перегружаем объект из базы, чтобы убрать возможные конфликты
                item = manufactureItemsDao.findOne(item.getId());
                TransferEditor editor = new TransferEditor(item);
                editor.show();
                
                
            }
        });
    }

    public void reloadData(){
        List<BpmTransferToStockItem> transferItems = transferItemsDao.findByTransferToStockIdOrderByIdAsc(transfer.getId());
        List<BpmManufactureDayPlanLineBuildingObjectItem> avaliableToTransferItems = manufactureItemsDao.findAvaliableToTransferItems();
        buildingObjectItemsGrid.loadData(avaliableToTransferItems);
        transferItemsGrid.loadData(transferItems);
    }
    

    private class TransferEditor extends BpmWindow {
        private static final long serialVersionUID = 1L;

        private TransferEditor(final BpmManufactureDayPlanLineBuildingObjectItem buildingObjectItem) {
            super("Передать на склад");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            TextField articleTypeNameNameTextField = new TextField("Тип изделия");
            articleTypeNameNameTextField.setWidth("100%");
            articleTypeNameNameTextField.setValue(buildingObjectItem.getArticle().getArticleType().getName());
            articleTypeNameNameTextField.setReadOnly(true);
            layout.addComponent(articleTypeNameNameTextField);

            TextField articleNameTextField = new TextField("Наименование изделия");
            articleNameTextField.setWidth("100%");
            articleNameTextField.setValue(buildingObjectItem.getArticle().getName());
            articleNameTextField.setReadOnly(true);
            layout.addComponent(articleNameTextField);

            final ComboBox quantityComboBox = new ComboBox("Количество");
            quantityComboBox.setWidth("100%");
            quantityComboBox.setNullSelectionAllowed(false);
            quantityComboBox.setTextInputAllowed(false);

            int needToMapQuantity = buildingObjectItem.getQuantity() - buildingObjectItem.getStockQuantity();
            int i;
            for (i = 1; i <= needToMapQuantity; i++) {
                quantityComboBox.addItem(i);
            }
            quantityComboBox.select(i - 1);
            layout.addComponent(quantityComboBox);

            Button editButton = new Button("Добавить");
            editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    // Автоматический мэппинг
                    int needToMapQuantity = (int) quantityComboBox.getValue();
                    int transfered = 0;
                    if(needToMapQuantity > 0) {
                        
                        List<BpmBuildingPlanItem> avaliablePlanItems = buildingPlanItemDao.findAvaliableToTransferToStock(buildingObjectItem.getBuildingObject().getId(), buildingObjectItem.getArticle().getId());
                        
                        List<BpmTransferToStockItem> newTransferItems = new ArrayList<>();
                        HashSet<BpmBuildingPlanItem> updatedBuildingPlanItems = new HashSet<BpmBuildingPlanItem>();
                        int avaliablePlanItemsIndex = 0;
                        while(needToMapQuantity > 0 && avaliablePlanItemsIndex < avaliablePlanItems.size()){
                            BpmBuildingPlanItem bpmBuildingPlanItem = avaliablePlanItems.get(avaliablePlanItemsIndex);
                            avaliablePlanItemsIndex++;
                            int avaliable = bpmBuildingPlanItem.getProductionReserveQuantity() - bpmBuildingPlanItem.getProductionQuantity();
                            if(avaliable <= 0){
                                continue;
                            }
                            avaliable = avaliable > needToMapQuantity ? needToMapQuantity : avaliable;
                            
                            BpmTransferToStockItem transferItem = new BpmTransferToStockItem();
                            transferItem.setBuildingPlanItem(bpmBuildingPlanItem);
                            transferItem.setTransferToStock(transfer);
                            transferItem.setManufactureDayPlanLineBuildingObjectItem(buildingObjectItem);
                            transferItem.setQuantity(avaliable);
                            newTransferItems.add(transferItem);
                            bpmBuildingPlanItem.setProductionQuantity(bpmBuildingPlanItem.getProductionQuantity() + avaliable);
                            updatedBuildingPlanItems.add(bpmBuildingPlanItem);
                            transfered += avaliable;
                            needToMapQuantity -= avaliable;
                        }
                        if(needToMapQuantity > 0){
                            Notification notification = new Notification("Не удалось сопоставить с заявкой на строительство "+needToMapQuantity+" изделий", Notification.Type.ERROR_MESSAGE);
                            notification.setDelayMsec(2000);
                            notification.setPosition(Position.BOTTOM_RIGHT);
                            notification.show(Page.getCurrent());
                        }
                        buildingObjectItem.setStockQuantity(buildingObjectItem.getStockQuantity() + transfered);
                        manufactureItemsDao.save(buildingObjectItem);
                        buildingPlanItemDao.save(updatedBuildingPlanItems);
                        transferItemsDao.save(newTransferItems);
                    }
                    reloadData();
                    close();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }
    }
}
