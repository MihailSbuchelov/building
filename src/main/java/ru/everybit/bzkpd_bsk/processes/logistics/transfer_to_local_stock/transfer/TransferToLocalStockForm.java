package ru.everybit.bzkpd_bsk.processes.logistics.transfer_to_local_stock.transfer;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;

public class TransferToLocalStockForm extends BpmWizardForm {

    @Override
    protected void initWizardFormSteps() {
        addWizardFormStep(new TransferToLocalStockFormStep1(this));
        addWizardFormStep(new TransferToLocalStockFormStep2(this));
    }
}
