package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmBlockDao extends PagingAndSortingRepository<BpmBlock, Long> {
    List<BpmBlock> findByBuildingObjectIdOrderByPriorityAsc(Long buildingObjectId);
}
