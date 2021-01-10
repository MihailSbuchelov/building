package ru.everybit.bzkpd_bsk.processes.logistics.write_off_from_stock;

import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItem;
import ru.everybit.bzkpd_bsk.report.logistics.StockItemReport;

public class WriteOffFromStockForm extends BpmForm {
    private static final long serialVersionUID = 1L;
    //view
    private StockItemReport stockItemReport;
    private Button addButton;
    private Button removeButton;
    private BpmGrid writeOffItemsGrid;
    private Compare.Greater stockItemQuantityFilter;
    private Compare.Greater writeOffItemQuantityFilter;

    public WriteOffFromStockForm() {
        super(false);
    }

    @Override
    protected void initUiContent() {
        initStockItemsGrid();
        initButtonsLayout();
        initWriteOffItemsGrid();
    }

    private void initStockItemsGrid() {
        stockItemReport = new StockItemReport();
        stockItemReport.initView();
        formContentLayout.addComponent(stockItemReport.getView());

        stockItemReport.getStockItemsGrid().setSelectionMode(Grid.SelectionMode.SINGLE);
        stockItemReport.getStockItemsGrid().addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                addButton.setEnabled(stockItemReport.getStockItemsGrid().getSelectedRow() != null);
            }
        });
        stockItemReport.getStockItemsGrid().addFilter(stockItemQuantityFilter = new Compare.Greater("quantity", 0));
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
                eventRemoveButtonClick();
            }
        });
        horizontalLayout.addComponent(removeButton);
    }

    private void initWriteOffItemsGrid() {
        writeOffItemsGrid = new BpmGrid();
        writeOffItemsGrid.setWidth("100%");
        writeOffItemsGrid.setHeightMode(HeightMode.ROW);
        writeOffItemsGrid.setHeightByRows(7);
        writeOffItemsGrid.addGridColumn("buildingObject", String.class, "Объект");
        writeOffItemsGrid.addGridColumn("floor", String.class, "Этаж");
        writeOffItemsGrid.addGridColumn("block", String.class, "Блок-секция");
        writeOffItemsGrid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        writeOffItemsGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        writeOffItemsGrid.addGridColumn("quantity", Integer.class, "Количество");
        writeOffItemsGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                removeButton.setEnabled(writeOffItemsGrid.getSelectedRow() != null);
            }
        });
        writeOffItemsGrid.addFilter(writeOffItemQuantityFilter = new Compare.Greater("quantity", 0));
        formContentLayout.addComponent(writeOffItemsGrid);
    }

    private void updateWriteOffItem(BpmStockItem stockItem, int quantity) {
        if (writeOffItemsGrid.getContainerDataSource().getItem(stockItem) == null) {
            writeOffItemsGrid.addItem(stockItem);
            writeOffItemsGrid.setItemProperty(stockItem, "buildingObject", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getBuildingObject().getObject());
            writeOffItemsGrid.setItemProperty(stockItem, "floor", stockItem.getBuildingPlanItem().getProduct().getSector().getFloor().getName());
            writeOffItemsGrid.setItemProperty(stockItem, "block", stockItem.getBuildingPlanItem().getProduct().getSector().getBlock().getName());
            writeOffItemsGrid.setItemProperty(stockItem, "articleTypeName", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            writeOffItemsGrid.setItemProperty(stockItem, "articleName", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getName());
            writeOffItemsGrid.setItemProperty(stockItem, "quantity", 0);
        }
        quantity += (Integer) writeOffItemsGrid.getItemProperty(stockItem, "quantity");
        writeOffItemsGrid.setItemProperty(stockItem, "quantity", quantity);
    }

    private void eventRemoveButtonClick() {
        BpmStockItem stockItem = (BpmStockItem) writeOffItemsGrid.getSelectedRow();
        int quantity = (int) writeOffItemsGrid.getItemProperty(stockItem, "quantity");

        stockItemReport.getStockItemsGrid().removeFilter(stockItemQuantityFilter);
        stockItemReport.updateQuantity(stockItem, quantity);
        updateWriteOffItem(stockItem, -quantity);
        stockItemReport.getStockItemsGrid().addFilter(stockItemQuantityFilter);
    }

    @Override
    protected void submitButtonClick() {
        for (Object itemId : writeOffItemsGrid.getContainerDataSource().getItemIds()) {
            BpmStockItem stockItem = (BpmStockItem) itemId;

            int quantity = (int) writeOffItemsGrid.getItemProperty(stockItem, "quantity");

            BpmBuildingPlanItem buildingPlanItem = stockItem.getBuildingPlanItem();
            buildingPlanItem.setStockQuantity(buildingPlanItem.getStockQuantity() - quantity);
            buildingPlanItem.setProductionQuantity(buildingPlanItem.getProductionQuantity() - quantity);
//            buildingPlanItem.setProductionReserveQuantity(buildingPlanItem.getProductionReserveQuantity() - quantity);
//            buildingPlanItem.setQuantity(buildingPlanItem.getQuantity() - quantity);
            BpmDaoFactory.getBuildingPlanItemDao().save(buildingPlanItem);

            if (stockItem.getQuantity() - quantity == 0) {
                BpmDaoFactory.getStockItemDao().delete(stockItem);
            } else {
                stockItem.setQuantity(stockItem.getQuantity() - quantity);
                BpmDaoFactory.getStockItemDao().save(stockItem);
            }
        }
        super.submitButtonClick();
    }

    private class Editor extends BpmWindow {
        // model
        private BpmStockItem stockItem;

        // view
        private BpmComboBox quantityComboBox;

        private Editor() {
            super("Добавить изделие");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            stockItem = (BpmStockItem) stockItemReport.getStockItemsGrid().getSelectedRow();

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

            quantityComboBox = new BpmComboBox("Количество");
            quantityComboBox.setWidth("100%");
            quantityComboBox.setNullSelectionAllowed(false);
            quantityComboBox.setTextInputAllowed(false);
            int quantity = (int) stockItemReport.getStockItemsGrid().getItemProperty(stockItem, "quantity");
            int i;
            for (i = 1; i <= quantity; i++) {
                quantityComboBox.addItem(i);
            }
            quantityComboBox.selectFirstItem();
            layout.addComponent(quantityComboBox);

            Button editButton = new Button("Списать");
            editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    eventEditButtonClick();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }

        private void eventEditButtonClick() {
            stockItemReport.getStockItemsGrid().deselect(stockItem);

            writeOffItemsGrid.removeFilter(writeOffItemQuantityFilter);
            int quantity = (int) quantityComboBox.getValue();
            stockItemReport.updateQuantity(stockItem, -quantity);
            updateWriteOffItem(stockItem, quantity);
            writeOffItemsGrid.addFilter(writeOffItemQuantityFilter);
            close();
        }
    }
}
