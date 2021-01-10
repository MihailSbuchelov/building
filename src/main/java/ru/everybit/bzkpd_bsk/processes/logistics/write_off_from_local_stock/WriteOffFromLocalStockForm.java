package ru.everybit.bzkpd_bsk.processes.logistics.write_off_from_local_stock;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;

public class WriteOffFromLocalStockForm extends BpmWizardForm {

    @Override
    protected void initWizardFormSteps() {
        addWizardFormStep(new WriteOffFromLocalStockFormStep1(this));
        addWizardFormStep(new WriteOffFromLocalStockFormStep2(this));
    }
}
