package ru.everybit.bzkpd_bsk.processes.logistics.transfer_to_local_stock.posting;

import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;

import org.springframework.data.domain.Sort;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShipping;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShippingItem;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmProduct;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmSector;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostingToLocalStockForm extends BpmForm {
    // model
    private BpmShipping shipping;
    // view
    private BpmGrid shippingItemsGrid;
    private Button addButton;
    private Button removeButton;
    private BpmGrid localStockItemsGrid;

    @Override
    protected void initUiContent() {
        initPostingItemsFormModel();
        initShippingLayout();
        initShippingItemsGrid();
        initButtonsLayout();
        initLocalStockItemsGrid();
    }

    private void initPostingItemsFormModel() {
        shipping = BpmDaoFactory.getShippingDao().findOne((Long) getProcessVariable("shippingId"));
    }

    private void initShippingLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        formContentLayout.addComponent(horizontalLayout);

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

    private void initShippingItemsGrid() {
        formContentLayout.addComponent(new Label("Транспорт"));

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
                addButton.setEnabled(shippingItemsGrid.getSelectedRow() != null);
            }
        });
        formContentLayout.addComponent(shippingItemsGrid);

        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.floor.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.block.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.name"));
        for (BpmShippingItem shippingItem : BpmDaoFactory.getShippingItemDao().findByShipping(shipping, new Sort(orderList))) {
            updateShippingItem(shippingItem, shippingItem.getQuantity());
        }
    }

    private void updateShippingItem(BpmShippingItem shippingItem, int quantity) {
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
        shippingItemsGrid.setItemProperty(shippingItem, "volume", articleVolume * quantity);
        shippingItemsGrid.setItemProperty(shippingItem, "quantity", quantity);
    }

    private void initButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100px");
        horizontalLayout.setSpacing(true);
        formContentLayout.addComponent(horizontalLayout);
        formContentLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        addButton = new Button(FontAwesome.ARROW_DOWN);
        addButton.setEnabled(false);
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                new Editor().show();
            }
        });
        horizontalLayout.addComponent(addButton);

        removeButton = new Button(FontAwesome.ARROW_UP);
        removeButton.setEnabled(false);
        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventRemoveLocalStockItem();
            }
        });
        horizontalLayout.addComponent(removeButton);
    }

    private void initLocalStockItemsGrid() {
        localStockItemsGrid = new BpmGrid();
        localStockItemsGrid.setWidth("100%");
        localStockItemsGrid.setHeightMode(HeightMode.ROW);
        localStockItemsGrid.setHeightByRows(5);
        localStockItemsGrid.addGridColumn("floor", String.class, "Этаж");
        localStockItemsGrid.addGridColumn("block", String.class, "Блок-секция");
        localStockItemsGrid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        localStockItemsGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        localStockItemsGrid.addGridColumn("articleVolume", Float.class, "Объем изделия").setHidden(true);
        localStockItemsGrid.addGridColumn("quantity", Integer.class, "Количество");
        localStockItemsGrid.addGridColumn("volume", Float.class, "Объем, м3");
        localStockItemsGrid.prependFooterRow();
        localStockItemsGrid.addFooterSum("volume");
        localStockItemsGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                removeButton.setEnabled(localStockItemsGrid.getSelectedRow() != null);
            }
        });
        formContentLayout.addComponent(localStockItemsGrid);
    }

    private void updateLocalStockItem(BpmShippingItem shippingItem, int quantity) {
        if (localStockItemsGrid.getContainerDataSource().getItem(shippingItem) == null) {
            localStockItemsGrid.addItem(shippingItem);
            BpmProduct product = shippingItem.getBuildingPlanItem().getProduct();
            BpmSector sector = product.getSector();
            BpmArticle article = product.getBuildingObjectArticle().getArticle();
            localStockItemsGrid.setItemProperty(shippingItem, "floor", sector.getFloor().getName());
            localStockItemsGrid.setItemProperty(shippingItem, "block", sector.getBlock().getName());
            localStockItemsGrid.setItemProperty(shippingItem, "articleTypeName", article.getArticleType().getName());
            localStockItemsGrid.setItemProperty(shippingItem, "articleName", article.getName());
            localStockItemsGrid.setItemProperty(shippingItem, "articleVolume", article.getVolume());
        }
        Float articleVolume = (Float) localStockItemsGrid.getItemProperty(shippingItem, "articleVolume");
        localStockItemsGrid.setItemProperty(shippingItem, "volume", articleVolume * quantity);
        localStockItemsGrid.setItemProperty(shippingItem, "quantity", quantity);
    }

    private void eventAddShippingItem(BpmShippingItem shippingItem, int quantity) {
        int shippingItemQuantity = (int) shippingItemsGrid.getItemProperty(shippingItem, "quantity");
        shippingItemQuantity -= quantity;
        if (shippingItemQuantity > 0) {
            updateShippingItem(shippingItem, shippingItemQuantity);
        } else {
            shippingItemsGrid.removeItemProperty(shippingItem);
        }

        int localStockItemQuantity;
        if (localStockItemsGrid.getContainerDataSource().getItem(shippingItem) == null) {
            localStockItemQuantity = 0;
        } else {
            localStockItemQuantity = (int) localStockItemsGrid.getItemProperty(shippingItem, "quantity");
        }
        updateLocalStockItem(shippingItem, localStockItemQuantity + quantity);

        shippingItemsGrid.deselect(shippingItemsGrid.getSelectedRow());
    }

    private void eventRemoveLocalStockItem() {
        BpmShippingItem localStockItem = (BpmShippingItem) localStockItemsGrid.getSelectedRow();
        int shippingItemQuantity;
        if (shippingItemsGrid.getContainerDataSource().getItem(localStockItem) == null) {
            shippingItemQuantity = 0;
        } else {
            shippingItemQuantity = (int) shippingItemsGrid.getItemProperty(localStockItem, "quantity");
        }
        int localStockItemQuantity = (int) localStockItemsGrid.getItemProperty(localStockItem, "quantity");
        updateShippingItem(localStockItem, shippingItemQuantity + localStockItemQuantity);
        shippingItemsGrid.deselect(shippingItemsGrid.getSelectedRow());
        localStockItemsGrid.removeItemProperty(localStockItem);
    }

    @Override
    protected void submitButtonClick() {
        if (!shippingItemsGrid.getContainerDataSource().getItemIds().isEmpty()) {
            Notification notification = new Notification("Не все детали оприходованы", Notification.Type.ERROR_MESSAGE);
            notification.setDelayMsec(2000);
            notification.setPosition(Position.BOTTOM_RIGHT);
            notification.show(Page.getCurrent());
            return;
        }
        for (Object itemId : localStockItemsGrid.getContainerDataSource().getItemIds()) {
            BpmShippingItem shippingItem = (BpmShippingItem) itemId;
            BpmBuildingPlanItem buildingPlanItem = shippingItem.getBuildingPlanItem();
            int quantity = (int) localStockItemsGrid.getItemProperty(shippingItem, "quantity");
            buildingPlanItem.setStockQuantity(buildingPlanItem.getStockQuantity() - quantity);
            buildingPlanItem.setLocalStockQuantity(buildingPlanItem.getLocalStockQuantity() + quantity);
            BpmDaoFactory.getBuildingPlanItemDao().save(buildingPlanItem);
        }
        shipping.setStatus(BpmShipping.STATUS_DONE);
        shipping.setEndDate(new Date());
        BpmDaoFactory.getShippingDao().save(shipping);
        super.submitButtonClick();
    }

    private class Editor extends BpmWindow {
        // model
        private BpmShippingItem shippingItem;
        // view
        private ComboBox quantityComboBox;

        private Editor() {
            super("Добавить изделие");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            shippingItem = (BpmShippingItem) shippingItemsGrid.getSelectedRow();

            TextField articleTypeNameNameTextField = new TextField("Тип изделия");
            articleTypeNameNameTextField.setWidth("100%");
            articleTypeNameNameTextField.setValue(shippingItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            articleTypeNameNameTextField.setReadOnly(true);
            layout.addComponent(articleTypeNameNameTextField);

            TextField articleNameTextField = new TextField("Наименование изделия");
            articleNameTextField.setWidth("100%");
            articleNameTextField.setValue(shippingItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getName());
            articleNameTextField.setReadOnly(true);
            layout.addComponent(articleNameTextField);

            quantityComboBox = new ComboBox("Количество");
            quantityComboBox.setWidth("100%");
            quantityComboBox.setNullSelectionAllowed(false);
            quantityComboBox.setTextInputAllowed(false);
            int quantity = (int) shippingItemsGrid.getItemProperty(shippingItem, "quantity");
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
                    eventAddShippingItem(shippingItem, (int) quantityComboBox.getValue());
                    close();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }
    }
}
