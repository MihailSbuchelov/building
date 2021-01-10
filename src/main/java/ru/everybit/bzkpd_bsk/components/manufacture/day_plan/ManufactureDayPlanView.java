package ru.everybit.bzkpd_bsk.components.manufacture.day_plan;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.event.SelectionEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;

public class ManufactureDayPlanView implements BpmView {
    // model
    private ManufactureDayPlanModel model;
    private BpmComboBox manufactureDayPlanLineComboBox;
    private BpmComboBox buildingObjectComboBox;
    private BpmComboBox articleTypeComboBox;
    private BpmGrid itemGrid;
    private VerticalLayout layout;
    // control
    private ManufactureDayPlanControl control;
    private TextArea commentTextArea;

    public void setModel(ManufactureDayPlanModel model) {
        this.model = model;
    }

    public void setControl(ManufactureDayPlanControl control) {
        this.control = control;
    }

    @Override
    public void initView() {
        initLayout();
        initDateFieldAndManufactureDayPlanLineComboBox();
        initCommentTextArea();
        initBuildingObjectComboBoxAndArticleTypeComboBox();
        initItemGrid();
    }

    private void initLayout() {
        layout = new VerticalLayout();
        layout.setSpacing(true);
    }

    private void initCommentTextArea() {
        commentTextArea = new TextArea("Комментарий");
        commentTextArea.setRows(2);
        commentTextArea.setWidth("100%");
        commentTextArea.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                model.updateManufactureDayPlanLineManagerComment(commentTextArea.getValue());
            }
        });
        layout.addComponent(commentTextArea);
    }

    private void initDateFieldAndManufactureDayPlanLineComboBox() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        layout.addComponent(horizontalLayout);

        Label label = new Label("День");
        label.setWidth("45px");
        horizontalLayout.addComponent(label);
        horizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

        DateField dateField = new DateField();
        dateField.setResolution(Resolution.DAY);
        dateField.setValue(model.getManufacturePlanDay());
        dateField.setReadOnly(true);
        horizontalLayout.addComponent(dateField);
        horizontalLayout.setComponentAlignment(dateField, Alignment.MIDDLE_CENTER);

        HorizontalLayout dummyLayout = new HorizontalLayout();
        horizontalLayout.addComponent(dummyLayout);
        horizontalLayout.setExpandRatio(dummyLayout, 1f);

        manufactureDayPlanLineComboBox = new BpmComboBox();
        manufactureDayPlanLineComboBox.setTextInputAllowed(false);
        manufactureDayPlanLineComboBox.setNullSelectionAllowed(false);
        for (BpmManufactureDayPlanLine manufactureDayPlanLine : model.getManufactureDayPlanLineList()) {
            manufactureDayPlanLineComboBox.getContainerDataSource().addItem(manufactureDayPlanLine);
        }
        manufactureDayPlanLineComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                control.eventManufactureDayPlanLineSelected((BpmManufactureDayPlanLine) manufactureDayPlanLineComboBox.getValue());
                String comment = model.getManufactureDayPlanLineManagerComment();
                commentTextArea.setValue(comment == null ? "" : comment);
            }
        });
        horizontalLayout.addComponent(manufactureDayPlanLineComboBox);
        horizontalLayout.setComponentAlignment(manufactureDayPlanLineComboBox, Alignment.MIDDLE_CENTER);
    }

    private void initBuildingObjectComboBoxAndArticleTypeComboBox() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setSpacing(true);
        layout.addComponent(horizontalLayout);

        buildingObjectComboBox = new BpmComboBox();
        buildingObjectComboBox.setWidth("100%");
        buildingObjectComboBox.setTextInputAllowed(false);
        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                control.eventBuildingObjectSelected((BpmBuildingObject) buildingObjectComboBox.getValue());
            }
        });
        horizontalLayout.addComponent(buildingObjectComboBox);

        articleTypeComboBox = new BpmComboBox();
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.setNullSelectionAllowed(false);
        articleTypeComboBox.setTextInputAllowed(false);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                control.eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
            }
        });
        horizontalLayout.addComponent(articleTypeComboBox);
    }

    private void initItemGrid() {
        itemGrid = new BpmGrid();
        itemGrid.setWidth("100%");
        itemGrid.setSelectionMode(Grid.SelectionMode.NONE);
        itemGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        itemGrid.addGridColumn("manufactureMonthPlanQuantity", Integer.class, "Всего произведено").setHidden(true);
        itemGrid.addGridColumn("manufactureMonthPlanPlanQuantity", Integer.class, "Всего необходимо").setHidden(true);
        itemGrid.addGridColumn("manufactureMonthPlan", String.class, "Всего");
        itemGrid.addGridColumn("manufactureMonthPlanLineQuantity", Integer.class, "Всего на линии произведено").setHidden(true);
        itemGrid.addGridColumn("manufactureMonthPlanLinePlanQuantity", Integer.class, "Всего на линии необходимо").setHidden(true);
        itemGrid.addGridColumn("manufactureMonthPlanLine", String.class, "Всего на линии");
        itemGrid.addGridColumn("manufactureDayPlanLineQuantity", Integer.class, "Запланировано на день");
        itemGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                if (itemGrid.getSelectedRow() != null) {
                    control.eventEditManufactureDayPlanItems((BpmArticle) itemGrid.getSelectedRow());
                    itemGrid.select(null);
                }
            }
        });
        layout.addComponent(itemGrid);
    }

    @Override
    public Component getView() {
        return layout;
    }

    void selectFirstManufactureLine() {
        manufactureDayPlanLineComboBox.selectFirstItem();
    }

    public void updateBuildingObjectList() {
        Container container = buildingObjectComboBox.getContainerDataSource();
        container.removeAllItems();
        String totalItem = "Итого";
        container.addItem(totalItem);
        for (BpmBuildingObject buildingObject : model.getBuildingObjectList()) {
            container.addItem(buildingObject);
        }
        buildingObjectComboBox.setNullSelectionItemId(totalItem);
        if (buildingObjectComboBox.getValue() != null) {
            buildingObjectComboBox.setValue(totalItem);
        } else {
            control.eventBuildingObjectSelected(null);
        }
    }

    public void setManufactureDayPlanItemGridEditable(boolean editable) {
        itemGrid.setSelectionMode(editable ? Grid.SelectionMode.SINGLE : Grid.SelectionMode.NONE);
    }

    public void updateArticleTypes() {
        Container container = articleTypeComboBox.getContainerDataSource();
        container.removeAllItems();
        for (BpmArticleType articleType : model.getArticleTypesSortedById()) {
            container.addItem(articleType);
        }
        if (articleTypeComboBox.getValue() == null || !container.containsId(articleTypeComboBox.getValue())) {
            articleTypeComboBox.selectFirstItem();
        } else {
            control.eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
        }
    }

    public void updateManufactureDayPlanItems() {
        itemGrid.removeAllItems();

        BpmArticleType articleType = model.getArticleType();
        if (articleType == null) {
            return;
        }

        for (BpmArticle article : model.getArticlesSortedById()) {
            itemGrid.addItem(article);
            itemGrid.setItemProperty(article, "articleName", article.getName());

            int manufactureMonthPlanQuantity = model.getSumOfManufactureMonthPlanItemQuantityGroupByArticle(article);
            itemGrid.setItemProperty(article, "manufactureMonthPlanQuantity", manufactureMonthPlanQuantity);
            int manufactureMonthPlanPlanQuantity = model.getSumOfManufactureMonthPlanItemPlanQuantityGroupByArticle(article);
            itemGrid.setItemProperty(article, "manufactureMonthPlanPlanQuantity", manufactureMonthPlanPlanQuantity);
            itemGrid.setItemProperty(article, "manufactureMonthPlan", manufactureMonthPlanQuantity + " из " + manufactureMonthPlanPlanQuantity);

            int manufactureMonthPlanLineQuantity = model.getSumOfManufactureMonthPlanItemQuantityGroupByArticleAndSelectedManufactureLine(article);
            itemGrid.setItemProperty(article, "manufactureMonthPlanLineQuantity", manufactureMonthPlanLineQuantity);
            int manufactureMonthPlanLinePlanQuantity = model.getSumOfManufactureMonthPlanItemPlanQuantityGroupByArticleAndSelectedManufactureLine(article);
            itemGrid.setItemProperty(article, "manufactureMonthPlanLinePlanQuantity", manufactureMonthPlanLinePlanQuantity);
            itemGrid.setItemProperty(article, "manufactureMonthPlanLine", manufactureMonthPlanLineQuantity + " из " + manufactureMonthPlanLinePlanQuantity);
            updateManufactureDayPlanItem(article);
        }
    }

    public void updateManufactureDayPlanItem(BpmArticle article) {
        int manufactureDayPlanLineQuantity = model.getManufactureDayPlanItemQuantity(article);
        itemGrid.setItemProperty(article, "manufactureDayPlanLineQuantity", manufactureDayPlanLineQuantity);
    }
}