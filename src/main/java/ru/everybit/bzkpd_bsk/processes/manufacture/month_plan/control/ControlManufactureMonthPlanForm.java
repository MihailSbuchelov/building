package ru.everybit.bzkpd_bsk.processes.manufacture.month_plan.control;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;
import ru.everybit.bzkpd_bsk.processes.manufacture.month_plan.create.CreateManufacturePlanForm;
import ru.everybit.bzkpd_bsk.report.manufacture.ManufactureMonthPlanReport;

public class ControlManufactureMonthPlanForm extends BpmForm {
    // model
    private BpmManufactureMonthPlan manufactureMonthPlan;
    // view
    private ManufactureMonthPlanReport manufactureMonthPlanReport;

    public ControlManufactureMonthPlanForm() {
        super(false);
    }

    @Override
    public void initUiContent() {
        initManufactureMonthPlanModel();
        initManufactureMonthPlanDateField();
        initManufactureMonthPlanReport();
        initStartManufactureDayPlanProcessButton();
        manufactureMonthPlanReport.selectFirstArticleType();
    }

    private void initManufactureMonthPlanModel() {
        manufactureMonthPlan = BpmDaoFactory.getManufactureMonthPlanDao().findOne(
                (Long) getProcessVariable(CreateManufacturePlanForm.VARIABLE_MANUFACTURE_MONTH_PLAN_ID));
    }

    private void initManufactureMonthPlanDateField() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        formContentLayout.addComponent(layout);

        Label label = new Label("Месяц");
        layout.addComponent(label);
        layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

        DateField dateField = new DateField();
        dateField.setResolution(Resolution.MONTH);
        dateField.setValue(manufactureMonthPlan.getMonth());
        dateField.setReadOnly(true);
        layout.addComponent(dateField);
    }

    private void initManufactureMonthPlanReport() {
        manufactureMonthPlanReport = new ManufactureMonthPlanReport();
        manufactureMonthPlanReport.setManufactureMonthPlan(manufactureMonthPlan);
        manufactureMonthPlanReport.initView();
        formContentLayout.addComponent(manufactureMonthPlanReport.getView());
    }

    private void initStartManufactureDayPlanProcessButton() {
        Button startManufactureDayPlanProcessButton = new Button("Сформировать задания на день");
        startManufactureDayPlanProcessButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        startManufactureDayPlanProcessButton.addStyleName(BpmThemeStyles.BUTTON_SMALL);
        startManufactureDayPlanProcessButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Map<String, Object> variables = new HashMap<>();
                variables.put(BpmProcessModel.TYPE_VARIABLE, "План производства");
                startSubProcess("ManufactureDayPlanProcess", "ПД", variables);
            }
        });
        formContentLayout.addComponent(startManufactureDayPlanProcessButton);
    }
}
