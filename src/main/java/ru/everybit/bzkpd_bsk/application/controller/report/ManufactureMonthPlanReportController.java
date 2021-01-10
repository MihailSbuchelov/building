package ru.everybit.bzkpd_bsk.application.controller.report;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;
import ru.everybit.bzkpd_bsk.report.manufacture.ManufactureMonthPlanReport;

public class ManufactureMonthPlanReportController {
    public static void init(BpmWorkingArea workingArea) {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);
        workingArea.setContent(layout);

        Label label = new Label("План производства");
        label.addStyleName(BpmThemeStyles.LABEL_H2);
        layout.addComponent(label);

        HorizontalLayout dateHorizontalLayout = new HorizontalLayout();
        dateHorizontalLayout.setSpacing(true);
        layout.addComponent(dateHorizontalLayout);

        Label monthLabel = new Label("Месяц");
        dateHorizontalLayout.addComponent(monthLabel);
        dateHorizontalLayout.setComponentAlignment(monthLabel, Alignment.MIDDLE_LEFT);

        final BpmComboBox comboBox = new BpmComboBox();
        comboBox.setTextInputAllowed(false);
        comboBox.setNullSelectionAllowed(false);
        for (BpmManufactureMonthPlan manufactureMonthPlan : BpmDaoFactory.getManufactureMonthPlanDao().
                findAll(new Sort(Sort.Direction.DESC, "month"))) {
            comboBox.addItem(manufactureMonthPlan);
        }
        dateHorizontalLayout.addComponent(comboBox);

        final HorizontalLayout reportHorizontalLayout = new HorizontalLayout();
        reportHorizontalLayout.setWidth("100%");
        layout.addComponent(reportHorizontalLayout);

        comboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                reportHorizontalLayout.removeAllComponents();
                ManufactureMonthPlanReport report = new ManufactureMonthPlanReport();
                report.setManufactureMonthPlan((BpmManufactureMonthPlan) comboBox.getValue());
                report.initView();
                report.selectFirstArticleType();
                reportHorizontalLayout.addComponent(report.getView());
            }
        });
        comboBox.selectFirstItem();
    }
}
