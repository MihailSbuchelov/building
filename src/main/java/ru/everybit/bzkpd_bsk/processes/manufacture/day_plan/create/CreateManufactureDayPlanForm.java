package ru.everybit.bzkpd_bsk.processes.manufacture.day_plan.create;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;

public class CreateManufactureDayPlanForm extends BpmWizardForm {
    @Override
    protected void initWizardFormSteps() {
        addWizardFormStep(new CreateManufactureDayPlanFormStep1(this));
        addWizardFormStep(new CreateManufactureDayPlanFormStep2(this));
        addWizardFormStep(new CreateManufactureDayPlanFormStep3(this));
    }
}
