package ru.everybit.bzkpd_bsk.processes.logistics.transfer_to_local_stock.transfer;

import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShipping;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShippingItem;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItem;

import java.util.ArrayList;
import java.util.List;

public class TransferToLocalStockFormStep2 extends BpmWizardFormStep {
    // model
    private BpmShipping shipping;
    private float stockTotal;
    // view
    private BpmGrid stockItemsGrid;
    private Button addButton;
    private Button removeButton;
    private BpmGrid shippingItemsGrid;
    private TextField stockTotalTextField;

    public TransferToLocalStockFormStep2(BpmWizardForm wizardForm) {
        super(TransferToLocalStockFormStep2.class.getSimpleName(), wizardForm);
        stockTotal = 0f;
    }

    @Override
    public void init() {
        initTransferItemsFormStep2Model();
        initShippingLayout();
        initStockItemsHeaderLayout();
        initStockItemsGrid();
        initButtonsLayout();
        initShippingItemsHeaderLayout();
        initShippingItemsGrid();
    }

    private void initTransferItemsFormStep2Model() {
        shipping = BpmDaoFactory.getShippingDao().findOne((Long) getForm().getProcessVariable("shippingId"));
    }

    private void initShippingLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        wizardFormStepLayout.addComponent(horizontalLayout);

        Label dateLabel = new Label("День");
        horizontalLayout.addComponent(dateLabel);
        horizontalLayout.setComponentAlignment(dateLabel, Alignment.MIDDLE_LEFT);

        DateField dateField = new DateField();
        dateField.setResolution(Resolution.DAY);
        dateField.setValue(shipping.getStartDate());
        dateField.setReadOnly(true);
        horizontalLayout.addComponent(dateField);

        Label shippingNumberLabel = new Label("Рейс");
        horizontalLayout.addComponent(shippingNumberLabel);
        horizontalLayout.setComponentAlignment(shippingNumberLabel, Alignment.MIDDLE_LEFT);

        TextField shippingNumberTextField = new TextField();
        shippingNumberTextField.setValue(String.valueOf(shipping.getShippingNumber()));
        shippingNumberTextField.setReadOnly(true);
        horizontalLayout.addComponent(shippingNumberTextField);

        Label buildingObjectLabel = new Label("Объект");
        horizontalLayout.addComponent(buildingObjectLabel);
        horizontalLayout.setComponentAlignment(buildingObjectLabel, Alignment.MIDDLE_LEFT);

        TextField buildingObjectTextField = new TextField();
        buildingObjectTextField.setValue(String.valueOf(shipping.getBuildingObject()));
        buildingObjectTextField.setReadOnly(true);
        horizontalLayout.addComponent(buildingObjectTextField);
    }

    private void initStockItemsHeaderLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setWidth("100%");
        wizardFormStepLayout.addComponent(layout);

        Label stockLabel = new Label("Склад");
        stockLabel.setWidth("70px");
        layout.addComponent(stockLabel);
        layout.setComponentAlignment(stockLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout dummyLayout = new HorizontalLayout();
        layout.addComponent(dummyLayout);
        layout.setExpandRatio(dummyLayout, 1);

        Label totalLabel = new Label("Всего на складе, м3");
        totalLabel.setWidth("140px");
        layout.addComponent(totalLabel);
        layout.setComponentAlignment(totalLabel, Alignment.MIDDLE_LEFT);

        stockTotalTextField = new TextField();
        stockTotalTextField.setWidth("50px");
        for (BpmStockItem stockItem : BpmDaoFactory.getStockItemDao().findAll()) {
            stockTotal += stockItem.getQuantity() * stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume();
        }
        updateStockTotal();
        layout.addComponent(stockTotalTextField);
    }

    private void updateStockTotal() {
        stockTotalTextField.setReadOnly(false);
        stockTotalTextField.setValue(String.format("%.2f", stockTotal));
        stockTotalTextField.setReadOnly(true);
    }

    private void initStockItemsGrid() {
        stockItemsGrid = new BpmGrid();
        stockItemsGrid.setWidth("100%");
        stockItemsGrid.setHeightMode(HeightMode.ROW);
        stockItemsGrid.setHeightByRows(4);
        stockItemsGrid.addGridColumn("floor", String.class, "Этаж");
        stockItemsGrid.addGridColumn("block", String.class, "Блок-секция");
        stockItemsGrid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        stockItemsGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        stockItemsGrid.addGridColumn("articleVolume", Float.class, "Объем изделия").setHidden(true);
        stockItemsGrid.addGridColumn("quantity", Integer.class, "Количество");
        stockItemsGrid.addGridColumn("volume", Float.class, "Объем, м3");
        stockItemsGrid.prependFooterRow();
        stockItemsGrid.addFooterSum("volume");
        stockItemsGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                addButton.setEnabled(stockItemsGrid.getSelectedRow() != null);
            }
        });
        wizardFormStepLayout.addComponent(stockItemsGrid);

        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.floor.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.block.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.name"));
        for (BpmStockItem stockItem : BpmDaoFactory.getStockItemDao().
                findByBuildingPlanItemProductBuildingObjectArticleBuildingObject(shipping.getBuildingObject(), new Sort(orderList))) {
            updateStockItem(stockItem);
        }
    }

    private void updateStockItem(BpmStockItem stockItem) {
        if (stockItemsGrid.getContainerDataSource().getItem(stockItem) == null) {
            stockItemsGrid.addItem(stockItem);
            stockItemsGrid.setItemProperty(stockItem, "floor", stockItem.getBuildingPlanItem().getProduct().getSector().getFloor().getName());
            stockItemsGrid.setItemProperty(stockItem, "block", stockItem.getBuildingPlanItem().getProduct().getSector().getBlock().getName());
            stockItemsGrid.setItemProperty(stockItem, "articleTypeName", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            stockItemsGrid.setItemProperty(stockItem, "articleName", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getName());
            stockItemsGrid.setItemProperty(stockItem, "articleVolume", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume());
        }
        Float articleVolume = (Float) stockItemsGrid.getItemProperty(stockItem, "articleVolume");
        stockItemsGrid.setItemProperty(stockItem, "volume", articleVolume * stockItem.getQuantity());
        stockItemsGrid.setItemProperty(stockItem, "quantity", stockItem.getQuantity());
    }

    private void initButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100px");
        horizontalLayout.setSpacing(true);
        wizardFormStepLayout.addComponent(horizontalLayout);
        wizardFormStepLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        addButton = new Button(FontAwesome.ARROW_DOWN);
        addButton.setEnabled(false);
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventAddButtonClick();
            }
        });
        horizontalLayout.addComponent(addButton);

        removeButton = new Button(FontAwesome.ARROW_UP);
        removeButton.setEnabled(false);
        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventRemoveButtonClick();
            }
        });
        horizontalLayout.addComponent(removeButton);
    }

    private void initShippingItemsHeaderLayout() {
        wizardFormStepLayout.addComponent(new Label("Транспорт"));
    }

    private void initShippingItemsGrid() {
        shippingItemsGrid = new BpmGrid();
        shippingItemsGrid.setWidth("100%");
        shippingItemsGrid.setHeightMode(HeightMode.ROW);
        shippingItemsGrid.setHeightByRows(5);
        shippingItemsGrid.addGridColumn("floor", String.class, "Этаж");
        shippingItemsGrid.addGridColumn("block", String.class, "Блок-секция");
        shippingItemsGrid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        shippingItemsGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        shippingItemsGrid.addGridColumn("articleVolume", Float.class, "Объем изделия").setHidden(true);
        shippingItemsGrid.addGridColumn("quantity", Integer.class, "Количество");
        shippingItemsGrid.addGridColumn("volume", Float.class, "Объем, м3");
        shippingItemsGrid.prependFooterRow();
        shippingItemsGrid.addFooterSum("volume");
        shippingItemsGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                removeButton.setEnabled(shippingItemsGrid.getSelectedRow() != null);
            }
        });
        wizardFormStepLayout.addComponent(shippingItemsGrid);

        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.floor.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.block.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.name"));
        for (BpmShippingItem shippingItem : BpmDaoFactory.getShippingItemDao().findByShipping(shipping, new Sort(orderList))) {
            updateShippingItem(shippingItem);
        }
    }

    private void updateShippingItem(BpmShippingItem shippingItem) {
        if (shippingItemsGrid.getContainerDataSource().getItem(shippingItem) == null) {
            shippingItemsGrid.addItem(shippingItem);
            shippingItemsGrid.setItemProperty(shippingItem, "floor", shippingItem.getBuildingPlanItem().getProduct().getSector().getFloor().getName());
            shippingItemsGrid.setItemProperty(shippingItem, "block", shippingItem.getBuildingPlanItem().getProduct().getSector().getBlock().getName());
            shippingItemsGrid.setItemProperty(shippingItem, "articleTypeName", shippingItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            shippingItemsGrid.setItemProperty(shippingItem, "articleName", shippingItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getName());
            Float articleVolume = shippingItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume();
            shippingItemsGrid.setItemProperty(shippingItem, "articleVolume", articleVolume);
        }
        Float articleVolume = (Float) shippingItemsGrid.getItemProperty(shippingItem, "articleVolume");
        shippingItemsGrid.setItemProperty(shippingItem, "volume", articleVolume * (int) shippingItem.getQuantity());
        shippingItemsGrid.setItemProperty(shippingItem, "quantity", shippingItem.getQuantity());
    }

    private void eventAddButtonClick() {
        new Editor().show();
    }

    private void eventRemoveButtonClick() {
        BpmShippingItem shippingItem = (BpmShippingItem) shippingItemsGrid.getSelectedRow();
        BpmStockItem stockItem = null;
        for (Object itemId : stockItemsGrid.getContainerDataSource().getItemIds()) {
            if (((BpmStockItem) itemId).getBuildingPlanItem().getId().equals(shippingItem.getBuildingPlanItem().getId())) {
                stockItem = (BpmStockItem) itemId;
                break;
            }
        }
        if (stockItem == null) {
            stockItem = new BpmStockItem();
            stockItem.setBuildingPlanItem(shippingItem.getBuildingPlanItem());
            stockItem.setQuantity(0);
        }
        stockItem.setQuantity(stockItem.getQuantity() + shippingItem.getQuantity());
        stockTotal += shippingItem.getQuantity() * stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume();
        updateStockTotal();
        stockItem = BpmDaoFactory.getStockItemDao().save(stockItem);
        updateStockItem(stockItem);

        shippingItemsGrid.removeItemProperty(shippingItem);
        BpmDaoFactory.getShippingItemDao().delete(shippingItem);

        shippingItemsGrid.deselect(shippingItemsGrid.getSelectedRow());
    }

    @Override
    public void eventNextButtonClick() {
        shipping.setStatus(BpmShipping.STATUS_IN_PROGRESS);
        BpmDaoFactory.getShippingDao().save(shipping);
    }

    private class Editor extends BpmWindow {
        // model
        private BpmStockItem stockItem;
        // view
        private ComboBox quantityComboBox;

        private Editor() {
            super("Добавить изделие");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            stockItem = (BpmStockItem) stockItemsGrid.getSelectedRow();

            TextField articleTypeNameNameTextField = new TextField("Тип изделия");
            articleTypeNameNameTextField.setWidth("100%");
            articleTypeNameNameTextField.setValue(stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            articleTypeNameNameTextField.setReadOnly(true);
            layout.addComponent(articleTypeNameNameTextField);

            TextField articleNameTextField = new TextField("Наименование изделия");
            articleNameTextField.setWidth("100%");
            articleNameTextField.setValue(stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getName());
            articleNameTextField.setReadOnly(true);
            layout.addComponent(articleNameTextField);

            quantityComboBox = new ComboBox("Количество");
            quantityComboBox.setWidth("100%");
            quantityComboBox.setNullSelectionAllowed(false);
            quantityComboBox.setTextInputAllowed(false);
            int quantity = (int) stockItemsGrid.getItemProperty(stockItem, "quantity");
            int i;
            for (i = 1; i <= quantity; i++) {
                quantityComboBox.addItem(i);
            }
            quantityComboBox.select(i - 1);
            layout.addComponent(quantityComboBox);

            Button editButton = new Button("Добавить");
            editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    eventButtonClick();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }

        private void eventButtonClick() {
            int quantity = (int) quantityComboBox.getValue();

            stockItem.setQuantity(stockItem.getQuantity() - quantity);
            stockTotal -= quantity * stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume();
            updateStockTotal();
            if (stockItem.getQuantity() > 0) {
                stockItem = BpmDaoFactory.getStockItemDao().save(stockItem);
                updateStockItem(stockItem);
            } else {
                stockItemsGrid.removeItemProperty(stockItem);
                BpmDaoFactory.getStockItemDao().delete(stockItem);
            }

            BpmShippingItem shippingItem = null;
            for (Object itemId : shippingItemsGrid.getContainerDataSource().getItemIds()) {
                if (((BpmShippingItem) itemId).getBuildingPlanItem().getId().equals(stockItem.getBuildingPlanItem().getId())) {
                    shippingItem = (BpmShippingItem) itemId;
                    break;
                }
            }
            if (shippingItem == null) {
                shippingItem = new BpmShippingItem();
                shippingItem.setShipping(shipping);
                shippingItem.setBuildingPlanItem(stockItem.getBuildingPlanItem());
                shippingItem.setQuantity(0);
            }
            shippingItem.setQuantity(shippingItem.getQuantity() + quantity);
            BpmDaoFactory.getShippingItemDao().save(shippingItem);
            updateShippingItem(shippingItem);

            stockItemsGrid.deselect(shippingItemsGrid.getSelectedRow());
            close();
        }
    }
}
