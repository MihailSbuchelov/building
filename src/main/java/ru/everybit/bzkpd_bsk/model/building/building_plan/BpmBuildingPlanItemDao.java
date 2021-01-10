package ru.everybit.bzkpd_bsk.model.building.building_plan;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmFloor;

import java.util.List;

public interface BpmBuildingPlanItemDao extends JpaRepository<BpmBuildingPlanItem, Long> {
    @Query(value = "select bpm_building_plan_item.* from bpm_building_plan_item, bpm_building_plan_sector, bpm_block, bpm_floor "+
            "where bpm_building_plan_sector.id = bpm_building_plan_item.bpm_building_plan_sector_id "+
            "and bpm_floor.id = bpm_building_plan_sector.bpm_floor_id "+
            "and bpm_block.id = bpm_building_plan_sector.bpm_block_id "+
            "and bpm_building_plan_sector.bpm_building_plan_id = ? "+
            "order by bpm_floor.priority, bpm_block.priority", nativeQuery = true)
    List<BpmBuildingPlanItem> findByBuildingPlanSectorBuildingPlan(Long buildingPlanId);

    List<BpmBuildingPlanItem> findByBuildingPlanSectorBuildingPlan(BpmBuildingPlan buildingPlan, Sort sort);

    @Query(value = "select bpm_building_plan_item.* from bpm_building_plan_item, bpm_building_plan_sector, bpm_floor " +
            "where bpm_building_plan_item.bpm_building_plan_sector_id = bpm_building_plan_sector.id " +
            "and bpm_building_plan_sector.bpm_floor_id = bpm_floor.id " +
            "and bpm_floor.bpm_building_object_id = ?", nativeQuery = true)
    List<BpmBuildingPlanItem> findByBuildingObjectId(Long id);

    @Query(value = "select sum(bpm_building_plan_item.local_stock_quantity * bpm_article.volume) \n" +
            "from bpm_building_plan_item, bpm_product, bpm_building_object_article, bpm_article \n" +
            "where\n" +
            "    bpm_building_plan_item.bpm_product_id = bpm_product.id\n" +
            "    and bpm_product.bpm_building_object_article_id = bpm_building_object_article.id\n" +
            "    and bpm_building_object_article.bpm_article_id = bpm_article.id \n" +
            "    and bpm_building_object_article.bpm_building_object_id = ?", nativeQuery = true)
    Float totalVolume(Long buildingObjectId);

    List<BpmBuildingPlanItem> findByBuildingPlanSectorFloor(BpmFloor floor);

    List<BpmBuildingPlanItem> findByProductBuildingObjectArticleBuildingObject(BpmBuildingObject buildingObject, Sort orders);

    @Query(value = "select bpi.* from bpm_building_plan_item bpi " +
            "inner join bpm_building_plan_sector bps on bps.id = bpi.bpm_building_plan_sector_id " +
            "inner join bpm_floor f on f.id = bps.bpm_floor_id " +
            "inner join bpm_block b on b.id = bps.bpm_block_id " +
            "inner join bpm_product p on p.id = bpi.bpm_product_id " +
            "inner join bpm_building_object_article boai on boai.id = p.bpm_building_object_article_id " +
            "where f.bpm_building_object_id = ? " +
            "and boai.bpm_article_id = ? " +
            "and bpi.production_reserve_quantity > bpi.production_quantity " +
            "and exists(select id from bpm_manufacture_month_plan_for_building_plan p2p where p2p.bpm_building_plan_id = bps.bpm_building_plan_id) " +
            "order by f.priority, b.priority ", nativeQuery = true)
    List<BpmBuildingPlanItem> findAvaliableToTransferToStock(Long buildingObjectId, Long articleId);

}
