package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmBuildingObjectArticleDao extends PagingAndSortingRepository<BpmBuildingObjectArticle, Long> {
    List<BpmBuildingObjectArticle> findByBuildingObjectAndArticleArticleTypeAndArticleDeletedOrderByIdAsc(BpmBuildingObject buildingObject, BpmArticleType articleType, boolean b);

    List<BpmBuildingObjectArticle> findByBuildingObjectAndArticleDeletedOrderByIdAsc(BpmBuildingObject buildingObject, boolean deleted);
}