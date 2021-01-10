package ru.everybit.bzkpd_bsk.processes.building.day_plan;

import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.components.building.BpmBuildingObjectLayout;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.components.building.BuildingPlanReportGrid;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReport;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;

public class DayReportForm extends BpmForm {
    private static final long serialVersionUID = 1L;

    @Override
    public void initUiContent() {
        
        Long buildingObjectId = (Long) getParentProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        Long buildingPlanId = (Long) getParentProcessVariable(BpmBuildingProcessModel.BUILDING_PLAN_ID);
        final Long buildingPlanReportId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_PLAN_REPORT_ID);

        BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
        List<BpmBuildingPlanReport> reports = BpmDaoFactory.getBuildingPlanReportDao().findByBuildingPlanId(buildingPlanId);
        
        BpmBuildingObjectLayout buildingLayout = new BpmBuildingObjectLayout(buildingObject);
        formContentLayout.addComponent(buildingLayout);

        BuildingPlanReportGrid reportGrid = new BuildingPlanReportGrid(buildingPlanReportId, reports); 
        formContentLayout.addComponent(reportGrid);

    }

}
