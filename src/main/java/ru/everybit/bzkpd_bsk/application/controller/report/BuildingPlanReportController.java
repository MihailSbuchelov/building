package ru.everybit.bzkpd_bsk.application.controller.report;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.components.building.BpmBuildingPlanPanel;
import ru.everybit.bzkpd_bsk.components.building.main.BuildingPlansTable;

import java.util.List;

public class BuildingPlanReportController {
    public static void init(final BpmWorkingArea workingArea) {
        List<BpmBuildingObject> buildingObjects = BpmDaoFactory.getBuildingObjectDao().findAll();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);


        Label titleLabel = new Label("Строительство объекта");
        titleLabel.addStyleName(ValoTheme.LABEL_H2);
        layout.addComponent(titleLabel);

        FormLayout comboLayout = new FormLayout();
        BeanItemContainer<BpmBuildingObject> container = new BeanItemContainer<>(BpmBuildingObject.class, buildingObjects);
        ComboBox buildingObjectComboBox = new ComboBox("Объект");
        buildingObjectComboBox.setContainerDataSource(container);
        buildingObjectComboBox.setWidth("260px");
        buildingObjectComboBox.setInputPrompt("Выберите объект строительства");
        buildingObjectComboBox.setItemCaptionPropertyId("object");
        comboLayout.addComponent(buildingObjectComboBox);
        layout.addComponent(comboLayout);

        final BeanItemContainer<BpmBuildingPlan> planContainer = new BeanItemContainer<>(BpmBuildingPlan.class);
        BuildingPlansTable buildingPlansTable = new BuildingPlansTable(planContainer);
        buildingPlansTable.addGeneratedColumn("button", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button button = new Button("Открыть");
                button.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        BpmBuildingPlan buildingPlan = planContainer.getItem(itemId).getBean();
                        workingArea.setContent(new BpmBuildingPlanPanel(buildingPlan.getBuildingObject(), buildingPlan));
                    }
                });
                return button;
            }
        });

        buildingPlansTable.setVisibleColumns("fromDate", "toDate", "status", "button");
        buildingPlansTable.setColumnHeaders("Дата начала", "Дата завершения", "Статус", "");

        layout.addComponent(buildingPlansTable);
        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                planContainer.removeAllItems();
                BpmBuildingObject value = (BpmBuildingObject) event.getProperty().getValue();
                if (value == null)
                    return;
                List<BpmBuildingPlan> plans = BpmDaoFactory.getBuildingPlanDao().findByBuildingObjectId(value.getId());
                planContainer.addAll(plans);
            }
        });

        workingArea.setContent(layout);
    }
}
