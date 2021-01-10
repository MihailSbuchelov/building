package ru.everybit.bzkpd_bsk.model.building;

import ru.everybit.bzkpd_bsk.model.BpmProcessModel;

public interface BpmBuildingProcessModel extends BpmProcessModel {
    final String BUILDING_OBJECT_ID = "buildingObjectId";
    final String BUILDING_MODEL_ATTACHMENT = "buildingObjectAttachment";
    final String BUILDING_PLAN_ID = "buildingPlanId";
    final String BUILDING_PLAN_REPORT_ID = "buildingPlanReportId";
}
