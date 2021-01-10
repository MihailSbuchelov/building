package ru.everybit.bzkpd_bsk.processes.building.main.create_picking_list;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;

public class CreatePickingListFormStep1 extends BpmWizardFormStep {
    private TextField floorCountTextField;
    private TextField blockCountTextField;

    public CreatePickingListFormStep1(BpmWizardForm wizardForm) {
        super(CreatePickingListFormStep1.class.getSimpleName(), wizardForm);
    }

    @Override
    public void initModel() {
    }

    @Override
    public void initView() {
        floorCountTextField = new TextField("Количество этажей");
        blockCountTextField = new TextField("Количество секций");

        FormLayout buildingLayout = new FormLayout();
        wizardFormStepLayout.addComponent(buildingLayout);
        buildingLayout.addComponent(floorCountTextField);
        buildingLayout.addComponent(blockCountTextField);
    }

    @Override
    public void initControl() {
    }

    @Override
    public void eventNextButtonClick() {
        Integer floorCount = Integer.valueOf(floorCountTextField.getValue());
        getForm().setTaskVariable(CreatePickingListForm.VARIABLE_FLOOR_COUNT, floorCount);
        Integer blockCount = Integer.valueOf(blockCountTextField.getValue());
        getForm().setTaskVariable(CreatePickingListForm.VARIABLE_BLOCK_COUNT, blockCount);
    }
}
