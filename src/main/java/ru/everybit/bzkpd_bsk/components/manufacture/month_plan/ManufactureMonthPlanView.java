package ru.everybit.bzkpd_bsk.components.manufacture.month_plan;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.event.SelectionEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;

public class ManufactureMonthPlanView implements BpmView {
    // model
    private ManufactureMonthPlanModel model;
    // view
    private DateField dateField;
    private BpmComboBox buildingObjectComboBox;
    private BpmComboBox articleTypeComboBox;
    private BpmGrid itemGrid;
    private VerticalLayout layout;
    // control
    private ManufactureMonthPlanControl control;

    public void setModel(ManufactureMonthPlanModel model) {
        this.model = model;
    }

    public void setControl(ManufactureMonthPlanControl control) {
        this.control = control;
    }

    @Override
    public void initView() {
        initDateField();
        initBuildingObjectComboBox();
        initArticleTypeComboBox();
        initItemGrid();
        initLayout();
    }

    private void initDateField() {
        dateField = new DateField();
        dateField.setCaption("Месяц");
        dateField.setResolution(Resolution.MONTH);
        dateField.setValue(model.getManufacturePlanMonth());
        dateField.setReadOnly(true);
    }

    private void initBuildingObjectComboBox() {
        buildingObjectComboBox = new BpmComboBox();
        buildingObjectComboBox.setCaption("Выберете объект");
        buildingObjectComboBox.setTextInputAllowed(false);
        Container container = buildingObjectComboBox.getContainerDataSource();
        String totalItem = "Итого";
        container.addItem(totalItem);
        for (BpmBuildingObject buildingObject : model.getBuildingObjectList()) {
            container.addItem(buildingObject);
        }
        buildingObjectComboBox.setNullSelectionItemId(totalItem);
        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                control.eventBuildingObjectSelected((BpmBuildingObject) buildingObjectComboBox.getValue());
            }
        });
    }

    private void initArticleTypeComboBox() {
        articleTypeComboBox = new BpmComboBox();
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.setNullSelectionAllowed(false);
        articleTypeComboBox.setTextInputAllowed(false);
        articleTypeComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                control.eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
            }
        });
    }

    private void initItemGrid() {
        itemGrid = new BpmGrid();
        itemGrid.setWidth("100%");
        itemGrid.setSelectionMode(Grid.SelectionMode.NONE);
        itemGrid.addColumn("articleId", Long.class).setHidden(true);
        itemGrid.addColumn("articleName").setHeaderCaption("Наименование изделия").setSortable(false);
        itemGrid.addColumn("articleVolume", Float.class).setHeaderCaption("Объем").setSortable(false);
        itemGrid.addColumn("planQuantity", Integer.class).setHeaderCaption("шт").setSortable(false);
        itemGrid.addFooterSum("planQuantity");
        itemGrid.addColumn("planVolume", Float.class).setHeaderCaption("м3").setSortable(false);
        itemGrid.addFooterSum("planVolume");
        Grid.HeaderRow prependHeaderRow = itemGrid.prependHeaderRow();
        Grid.HeaderCell cell = prependHeaderRow.join("articleName", "articleVolume");
        cell.setComponent(articleTypeComboBox);
        prependHeaderRow.join("planQuantity", "planVolume").setText("Заявка на месяц");

        for (BpmManufactureLine manufactureLine : model.getManufactureLineList()) {
            String manufactureQuantity = "quantity" + manufactureLine.getId();
            itemGrid.addColumn(manufactureQuantity, Integer.class).setHeaderCaption("шт").setSortable(false);
            String manufactureVolume = "volume" + manufactureLine.getId();
            itemGrid.addColumn(manufactureVolume, Float.class).setHeaderCaption("м3").setSortable(false);
            itemGrid.addFooterSum(manufactureVolume);
            prependHeaderRow.join(manufactureQuantity, manufactureVolume).setText(manufactureLine.getName());
        }

        itemGrid.prependFooterRow();

        itemGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                if (itemGrid.getSelectedRow() != null) {
                    control.eventEditManufacturePlanItems((BpmArticle) itemGrid.getSelectedRow());
                    itemGrid.select(null);
                }
            }
        });
    }

    private void initLayout() {
        layout = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(dateField);
        formLayout.addComponent(buildingObjectComboBox);
        layout.addComponent(formLayout);
        layout.addComponent(itemGrid);
    }

    @Override
    public Component getView() {
        return layout;
    }

    public void setManufactureMonthPlanItemGridEditable(boolean editable) {
        itemGrid.setSelectionMode(editable ? Grid.SelectionMode.SINGLE : Grid.SelectionMode.NONE);
    }

    public void updateArticleTypes() {
        Container container = articleTypeComboBox.getContainerDataSource();
        container.removeAllItems();
        for (BpmArticleType articleType : model.getArticleTypesSortedById()) {
            container.addItem(articleType);
        }
        if (articleTypeComboBox.getValue() == null) {
            articleTypeComboBox.selectFirstItem();
        } else {
            control.eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public void updateManufactureMonthPlanItems() {
        itemGrid.removeAllItems();

        BpmArticleType articleType = model.getArticleType();
        if (articleType == null) {
            return;
        }

        for (BpmArticle article : model.getArticlesSortedById()) {
            itemGrid.addItem(article);
            itemGrid.setItemProperty(article, "articleId", article.getId());
            itemGrid.setItemProperty(article, "articleName", article.getName());
            itemGrid.setItemProperty(article, "articleVolume", article.getVolume());
            Integer planQuantity = model.getSumOfBuildingPlanItemQuantityGroupByArticle(article);
            itemGrid.setItemProperty(article, "planQuantity", planQuantity);
            itemGrid.setItemProperty(article, "planVolume", planQuantity * article.getVolume());
            updateManufactureMonthPlanItemsForArticle(article);
        }
    }

    @SuppressWarnings("unchecked")
    void updateManufactureMonthPlanItemsForArticle(BpmArticle article) {
        for (BpmManufactureLine manufactureLine : model.getManufactureLineList()) {
            int quantity = model.getSumOfManufacturePlanItems(article, manufactureLine);
            itemGrid.setItemProperty(article, "quantity" + manufactureLine.getId(), quantity);
            Float articleVolume = (Float) itemGrid.getItemProperty(article, "articleVolume");
            itemGrid.setItemProperty(article, "volume" + manufactureLine.getId(), (float) quantity * articleVolume);
        }
    }
}