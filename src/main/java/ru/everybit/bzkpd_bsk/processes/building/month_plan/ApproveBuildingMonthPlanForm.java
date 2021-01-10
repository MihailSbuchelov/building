package ru.everybit.bzkpd_bsk.processes.building.month_plan;

import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.components.building.BpmCreateBuildingPlanLayout;

public class ApproveBuildingMonthPlanForm extends BpmForm {
    private BpmCreateBuildingPlanLayout bpmBuildingPlanLayout;

    public ApproveBuildingMonthPlanForm() {
        super(false);
    }

    @Override
    public void initUiContent() {
        // Загружаем информацию об Объекте строительства
        Long buildingObjectId = (Long) getParentProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
        bpmBuildingPlanLayout = new BpmCreateBuildingPlanLayout(buildingObject);
        formContentLayout.addComponent(bpmBuildingPlanLayout);
    }

    @Override
    protected void initModel() {
        super.initModel();
        Long buildingPlanId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_PLAN_ID);
        BpmBuildingPlan buildingPlan = BpmDaoFactory.getBuildingPlanDao().findOne(buildingPlanId);
        bpmBuildingPlanLayout.loadRecord(buildingPlan);
    }

    @Override
    protected void submitButtonClick() {
        BpmBuildingPlan buildingPlan = bpmBuildingPlanLayout.getRecord();
        buildingPlan.setStatus(BpmBuildingPlan.STATUS_APPROVED);
        BpmDaoFactory.getBuildingPlanDao().save(buildingPlan);
        super.submitButtonClick();
    }
}