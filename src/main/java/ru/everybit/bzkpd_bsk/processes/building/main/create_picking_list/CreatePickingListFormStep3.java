package ru.everybit.bzkpd_bsk.processes.building.main.create_picking_list;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.components.building.main.picking_list.PickingListControl;
import ru.everybit.bzkpd_bsk.components.building.main.picking_list.PickingListModel;
import ru.everybit.bzkpd_bsk.components.building.main.picking_list.PickingListView;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;

public class CreatePickingListFormStep3 extends BpmWizardFormStep {
    // model
    private PickingListModel pickingListModel;
    // view
    private PickingListView pickingListView;
    // control
    private PickingListControl pickingListControl;

    public CreatePickingListFormStep3(BpmWizardForm wizardForm) {
        super(CreatePickingListFormStep3.class.getSimpleName(), wizardForm);
        // model
        pickingListModel = new PickingListModel();
        // view
        pickingListView = new PickingListView(this);
        // control
        pickingListControl = new PickingListControl();
        // init
        pickingListView.setPickingListModel(pickingListModel);
        pickingListView.setPickingListControl(pickingListControl);
        pickingListControl.setModel(pickingListModel);
        pickingListControl.setView(pickingListView);
    }

    @Override
    public void initModel() {
        Long buildingObjectId = (Long) getForm().getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
        pickingListModel.setBuildingObject(buildingObject);
        pickingListModel.initModel();
    }

    @Override
    public void initView() {
        pickingListView.initView();
        wizardFormStepLayout.addComponent(pickingListView.getView());
    }

    @Override
    public void initControl() {
        pickingListControl.initControl();
    }
}