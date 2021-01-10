package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmProductDao extends PagingAndSortingRepository<BpmProduct, Long> {
    @Query(value = "select bpm_product.* from bpm_product, bpm_sector "
            + "where bpm_product.bpm_sector_id = bpm_sector.id "
            + "and bpm_sector.bpm_floor_id = ? "
            + "and bpm_sector.bpm_block_id = ?", nativeQuery = true)
    List<BpmProduct> findByFloorAndBlock(Long floorId, Long blockId);

    @Query(value = "select bpm_product.* from bpm_product, bpm_sector, bpm_floor "
            + "where bpm_product.bpm_sector_id = bpm_sector.id "
            + "and bpm_sector.bpm_floor_id = bpm_floor.id "
            + "and bpm_floor.bpm_building_object_id = ?", nativeQuery = true)
    List<BpmProduct> findByBuildingObjectId(Long buildingObjectId);

    List<BpmProduct> findByBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle);

    BpmProduct findByBuildingObjectArticleAndSector(BpmBuildingObjectArticle buildingObjectArticle, BpmSector sector);

    @Query(value = "select sum(bpm_product.quantity) " +
            "from bpm_product, bpm_building_object_article, bpm_article " +
            "where " +
            "    bpm_product.bpm_building_object_article_id = bpm_building_object_article.id " +
            "    and bpm_building_object_article.bpm_article_id = bpm_article.id " +
            "    and bpm_building_object_article.bpm_building_object_id = ? " +
            "    and bpm_article.type_id = ?", nativeQuery = true)
    Integer totalQuantity(Long buildingObjectId, Long articleTypeId);

    List<BpmProduct> findBySectorFloor(BpmFloor floor, Sort sort);

    List<BpmProduct> findByBuildingObjectArticleBuildingObject(BpmBuildingObject buildingObject, Sort sort);
}
