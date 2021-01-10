package ru.everybit.bzkpd_bsk.application.controller.report;

import com.vaadin.data.Property;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.report.building.PickingListReport;

public class PickingListReportController {
    public static void init(final BpmWorkingArea workingArea) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        workingArea.setContent(layout);

        Label label = new Label("Комплектовочная ведомость");
        label.addStyleName(BpmThemeStyles.LABEL_H2);
        layout.addComponent(label);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        layout.addComponent(formLayout);

        final BpmComboBox buildingObjectComboBox = new BpmComboBox("Объект");
        buildingObjectComboBox.setWidth("100%");
        buildingObjectComboBox.setNullSelectionAllowed(false);
        buildingObjectComboBox.setTextInputAllowed(false);
        for (BpmBuildingObject buildingObject : BpmDaoFactory.getBuildingObjectDao().findAll(new Sort(Sort.Direction.ASC, "object"))) {
            buildingObjectComboBox.addItem(buildingObject);
        }
        formLayout.addComponent(buildingObjectComboBox);

        final VerticalLayout reportLayout = new VerticalLayout();
        reportLayout.setWidth("100%");
        layout.addComponent(reportLayout);


        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                reportLayout.removeAllComponents();
                PickingListReport pickingListReport = new PickingListReport();
                pickingListReport.setBuildingObject((BpmBuildingObject) buildingObjectComboBox.getValue());
                pickingListReport.initView();
                reportLayout.addComponent(pickingListReport.getView());
            }
        });
        buildingObjectComboBox.selectFirstItem();
    }
}
