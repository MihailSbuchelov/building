package ru.everybit.bzkpd_bsk.model.building.building_plan;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmBuildingPlanReportDao extends PagingAndSortingRepository<BpmBuildingPlanReport, Long> {
    List<BpmBuildingPlanReport> findByBuildingPlanId(Long buildingPlanId);
}
