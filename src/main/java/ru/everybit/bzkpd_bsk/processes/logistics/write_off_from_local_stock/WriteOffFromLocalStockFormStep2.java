package ru.everybit.bzkpd_bsk.processes.logistics.write_off_from_local_stock;

import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;

import java.util.ArrayList;
import java.util.List;

public class WriteOffFromLocalStockFormStep2 extends BpmWizardFormStep {
    //view
    private BpmGrid localStockItemsGrid;
    private Button addButton;
    private Button removeButton;
    private BpmGrid writeOffItemsGrid;
    private Compare.Greater localStockItemQuantityFilter;
    private Compare.Greater writeOffItemQuantityFilter;
    private BpmBuildingObject buildingObject;

    public WriteOffFromLocalStockFormStep2(BpmWizardForm wizardForm) {
        super(WriteOffFromLocalStockFormStep2.class.getSimpleName(), wizardForm);
    }

    @Override
    public void init() {
        initModel();
        initHeaderLayout();
        initLocalStockItemsGrid();
        initButtonsLayout();
        initWriteOffItemsGrid();
    }

    @Override
    public void initModel() {
        buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne((Long) getForm().getTaskVariable("buildingObjectId"));
    }

    private void initHeaderLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        wizardFormStepLayout.addComponent(formLayout);

        TextField textField = new TextField("Объект");
        textField.setWidth("100%");
        textField.setValue(buildingObject.getObject());
        textField.setReadOnly(true);
        formLayout.addComponent(textField);
    }

    private void initLocalStockItemsGrid() {
        localStockItemsGrid = new BpmGrid();
        localStockItemsGrid.setWidth("100%");
        localStockItemsGrid.setHeightMode(HeightMode.ROW);
        localStockItemsGrid.setHeightByRows(7);
        localStockItemsGrid.addGridColumn("floor", String.class, "Этаж");
        localStockItemsGrid.addGridColumn("block", String.class, "Блок-секция");
        localStockItemsGrid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        localStockItemsGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        localStockItemsGrid.addGridColumn("quantity", Integer.class, "Количество");
        localStockItemsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        localStockItemsGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                addButton.setEnabled(localStockItemsGrid.getSelectedRow() != null);
            }
        });
        wizardFormStepLayout.addComponent(localStockItemsGrid);

        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "product.sector.floor.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "product.sector.block.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "product.buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "product.buildingObjectArticle.article.name"));
        List<BpmBuildingPlanItem> buildingPlanItemList = BpmDaoFactory.getBuildingPlanItemDao().
                findByProductBuildingObjectArticleBuildingObject(buildingObject, new Sort(orderList));
        for (BpmBuildingPlanItem buildingPlanItem : buildingPlanItemList) {
            if (buildingPlanItem.getLocalStockQuantity() == 0) {
                continue;
            }
            localStockItemsGrid.addItem(buildingPlanItem);
            localStockItemsGrid.setItemProperty(buildingPlanItem, "floor", buildingPlanItem.getProduct().getSector().getFloor().getName());
            localStockItemsGrid.setItemProperty(buildingPlanItem, "block", buildingPlanItem.getProduct().getSector().getBlock().getName());
            localStockItemsGrid.setItemProperty(buildingPlanItem, "articleTypeName", buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            localStockItemsGrid.setItemProperty(buildingPlanItem, "articleName", buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getName());
            localStockItemsGrid.setItemProperty(buildingPlanItem, "quantity", buildingPlanItem.getLocalStockQuantity());
        }
        localStockItemsGrid.addFilter(localStockItemQuantityFilter = new Compare.Greater("quantity", 0));
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
        wizardFormStepLayout.addComponent(writeOffItemsGrid);
    }

    private void updateItem(BpmGrid grid, BpmBuildingPlanItem buildingPlanItem, int quantity) {
        if (grid.getContainerDataSource().getItem(buildingPlanItem) == null) {
            grid.addItem(buildingPlanItem);
            grid.setItemProperty(buildingPlanItem, "floor", buildingPlanItem.getProduct().getSector().getFloor().getName());
            grid.setItemProperty(buildingPlanItem, "block", buildingPlanItem.getProduct().getSector().getBlock().getName());
            grid.setItemProperty(buildingPlanItem, "articleTypeName", buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            grid.setItemProperty(buildingPlanItem, "articleName", buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getName());
            grid.setItemProperty(buildingPlanItem, "quantity", 0);
        }
        quantity += (Integer) grid.getItemProperty(buildingPlanItem, "quantity");
        grid.setItemProperty(buildingPlanItem, "quantity", quantity);
    }

    private void eventRemoveButtonClick() {
        BpmBuildingPlanItem buildingPlanItem = (BpmBuildingPlanItem) writeOffItemsGrid.getSelectedRow();

        localStockItemsGrid.removeFilter(localStockItemQuantityFilter);
        int quantity = (int) writeOffItemsGrid.getItemProperty(buildingPlanItem, "quantity");
        updateItem(localStockItemsGrid, buildingPlanItem, quantity);
        updateItem(writeOffItemsGrid, buildingPlanItem, -quantity);
        localStockItemsGrid.addFilter(localStockItemQuantityFilter);
    }

    @Override
    public void eventNextButtonClick() {
        for (Object itemId : writeOffItemsGrid.getContainerDataSource().getItemIds()) {
            BpmBuildingPlanItem buildingPlanItem = (BpmBuildingPlanItem) itemId;
            int quantity = (int) writeOffItemsGrid.getItemProperty(buildingPlanItem, "quantity");

            buildingPlanItem.setLocalStockQuantity(buildingPlanItem.getLocalStockQuantity() - quantity);
//            buildingPlanItem.setStockQuantity(buildingPlanItem.getStockQuantity() - quantity);
            buildingPlanItem.setProductionQuantity(buildingPlanItem.getProductionQuantity() - quantity);
//            buildingPlanItem.setProductionReserveQuantity(buildingPlanItem.getProductionReserveQuantity() - quantity);
//            buildingPlanItem.setQuantity(buildingPlanItem.getQuantity() - quantity);
            BpmDaoFactory.getBuildingPlanItemDao().save(buildingPlanItem);
        }
    }

    private class Editor extends BpmWindow {
        // model
        private BpmBuildingPlanItem buildingPlanItem;

        // view
        private BpmComboBox quantityComboBox;

        private Editor() {
            super("Добавить изделие");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            buildingPlanItem = (BpmBuildingPlanItem) localStockItemsGrid.getSelectedRow();

            TextField articleTypeNameNameTextField = new TextField("Тип изделия");
            articleTypeNameNameTextField.setWidth("100%");
            articleTypeNameNameTextField.setValue(buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            articleTypeNameNameTextField.setReadOnly(true);
            layout.addComponent(articleTypeNameNameTextField);

            TextField articleNameTextField = new TextField("Наименование изделия");
            articleNameTextField.setWidth("100%");
            articleNameTextField.setValue(buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getName());
            articleNameTextField.setReadOnly(true);
            layout.addComponent(articleNameTextField);

            quantityComboBox = new BpmComboBox("Количество");
            quantityComboBox.setWidth("100%");
            quantityComboBox.setNullSelectionAllowed(false);
            quantityComboBox.setTextInputAllowed(false);
            int quantity = (int) localStockItemsGrid.getItemProperty(buildingPlanItem, "quantity");
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
            localStockItemsGrid.deselect(buildingPlanItem);

            writeOffItemsGrid.removeFilter(writeOffItemQuantityFilter);
            int quantity = (int) quantityComboBox.getValue();
            updateItem(localStockItemsGrid, buildingPlanItem, -quantity);
            updateItem(writeOffItemsGrid, buildingPlanItem, quantity);
            writeOffItemsGrid.addFilter(writeOffItemQuantityFilter);
            close();
        }
    }
}
