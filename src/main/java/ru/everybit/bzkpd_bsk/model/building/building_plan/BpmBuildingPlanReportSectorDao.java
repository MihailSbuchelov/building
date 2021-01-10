package ru.everybit.bzkpd_bsk.model.building.building_plan;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BpmBuildingPlanReportSectorDao extends PagingAndSortingRepository<BpmBuildingPlanReportSector, Long> {
    BpmBuildingPlanReportSector findByReportIdAndSectorId(Long reportId, Long sectorId);
    List<BpmBuildingPlanReportSector> findByReportId(Long reportId);
}
