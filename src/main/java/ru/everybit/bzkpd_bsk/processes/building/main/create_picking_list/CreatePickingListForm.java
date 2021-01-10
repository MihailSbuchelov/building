package ru.everybit.bzkpd_bsk.processes.building.main.create_picking_list;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;

public class CreatePickingListForm extends BpmWizardForm {
    static final String VARIABLE_FLOOR_COUNT = "FloorCount";
    static final String VARIABLE_BLOCK_COUNT = "BlockCount";

    @Override
    protected void initWizardFormSteps() {
        addWizardFormStep(new CreatePickingListFormStep1(this));
        addWizardFormStep(new CreatePickingListFormStep2(this));
        addWizardFormStep(new CreatePickingListFormStep3(this));
    }
}
