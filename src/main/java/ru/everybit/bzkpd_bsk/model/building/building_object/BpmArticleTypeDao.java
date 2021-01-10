package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BpmArticleTypeDao extends JpaRepository<BpmArticleType, Long> {
    List<BpmArticleType> findByDeletedOrderByIdAsc(boolean deleted);

    @Query(value = "select distinct bpm_article_type.* from bpm_article_type, bpm_article, bpm_building_object_article "+
                    "where bpm_article.type_id = bpm_article_type.id and bpm_article.is_deleted = false "+
                    "and bpm_building_object_article.bpm_article_id = bpm_article.id "+
                    "and bpm_article_type.is_deleted = false and bpm_building_object_article.bpm_building_object_id = ?",
                    nativeQuery = true)
    List<BpmArticleType> findByBuildingObjectId(Long buildingObjectId);

}

