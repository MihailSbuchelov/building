package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmBuildingObjectDao extends PagingAndSortingRepository<BpmBuildingObject, Long> {

    List<BpmBuildingObject> findByModelAttachmentNotNull();

    List<BpmBuildingObject> findAll();
}
