package ru.everybit.bzkpd_bsk.processes.building.month_plan;

import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.components.building.BpmCreateBuildingPlanLayout;

public class CreateBuildingMonthPlanForm extends BpmForm {
    private BpmCreateBuildingPlanLayout bpmBuildingPlanLayout;
    private BpmBuildingObject buildingObject;

    public CreateBuildingMonthPlanForm() {
        super(false);
    }

    @Override
    public void initUiContent() {
        // Загружаем информацию об Объекте строительства
        Long buildingObjectId = (Long) getParentProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
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
        buildingPlan = BpmDaoFactory.getBuildingPlanDao().save(buildingPlan);
        setProcessVariable(BpmBuildingProcessModel.BUILDING_PLAN_ID, buildingPlan.getId());
        StringBuilder builder = new StringBuilder();
        builder.append(buildingObject.getObject());
        builder.append("<br>");
        builder.append(BpmUtils.formatDate(buildingPlan.getFromDate()));
        builder.append(" - ");
        builder.append(BpmUtils.formatDate(buildingPlan.getToDate()));
        setProcessVariable(BpmProcessModel.TITLE_VARIABLE, builder.toString());
        super.submitButtonClick();
    }
}