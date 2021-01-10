package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmFloorDao extends PagingAndSortingRepository<BpmFloor, Long> {

    List<BpmFloor> findByBuildingObjectIdOrderByPriorityAsc(Long buildingObjectId);

    List<BpmFloor> findByBuildingObjectIdOrderByPriorityDesc(Long buildingObjectId);
}
