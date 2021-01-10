package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmSectorDao extends PagingAndSortingRepository<BpmSector, Long> {
    @Query(value = "select bpm_sector.* from bpm_sector, bpm_floor, bpm_block " +
            "where bpm_sector.bpm_floor_id = bpm_floor.id and bpm_sector.bpm_block_id = bpm_block.id " +
            "and bpm_floor.bpm_building_object_id = ? " +
            "order by bpm_floor.priority, bpm_block.priority", nativeQuery = true)
    List<BpmSector> findByBuildingObjectIdOrderByFloorPriorityAndBlockPriority(Long buildingObjectId);

    List<BpmSector> findByFloorBuildingObject(BpmBuildingObject buildingObject, Sort orders);
}
