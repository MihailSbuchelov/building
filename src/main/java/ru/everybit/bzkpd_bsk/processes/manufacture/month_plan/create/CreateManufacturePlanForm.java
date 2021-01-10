package ru.everybit.bzkpd_bsk.processes.manufacture.month_plan.create;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;

public class CreateManufacturePlanForm extends BpmWizardForm {
    public static final String VARIABLE_MANUFACTURE_MONTH_PLAN_ID = "manufactureMonthPlanId";

    @Override
    protected void initWizardFormSteps() {
        addWizardFormStep(new CreateManufacturePlanFormStep1(this));
        addWizardFormStep(new CreateManufacturePlanFormStep2(this));
    }
}
