package ru.everybit.bzkpd_bsk.model.manufacture.day_plan;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;

import java.util.List;

public interface BpmManufactureDayPlanLineBuildingObjectItemDao extends JpaRepository<BpmManufactureDayPlanLineBuildingObjectItem, Long> {
    BpmManufactureDayPlanLineBuildingObjectItem findByManufactureDayPlanLineAndBuildingObjectAndArticle(BpmManufactureDayPlanLine manufactureDayPlanLine, BpmBuildingObject buildingObject, BpmArticle article);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLineAndArticle(BpmManufactureDayPlanLine manufactureDayPlanLine, BpmArticle article);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLineOrderByArticleIdAsc(BpmManufactureDayPlanLine manufactureDayPlanLine);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLine(BpmManufactureDayPlanLine manufactureDayPlanLine, Sort orders);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLineManufactureDayPlan(BpmManufactureDayPlan manufactureDayPlan, Sort orders);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndArticleArticleTypeOrderByArticleNameAsc(BpmManufactureMonthPlan manufactureMonthPlan, BpmArticleType articleType);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndArticle(BpmManufactureMonthPlan manufactureMonthPlan, BpmArticle article);

    List<BpmManufactureDayPlanLineBuildingObjectItem> findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndBuildingObjectAndArticle(BpmManufactureMonthPlan manufactureMonthPlan, BpmBuildingObject buildingObject, BpmArticle article);

    int countByManufactureDayPlanLine(BpmManufactureDayPlanLine manufactureDayPlanLine);

    @Query(value = "select * from bpm_manufacture_day_plan_line_building_object_item i where i.quantity > i.stock_quantity",
            nativeQuery = true)
    List<BpmManufactureDayPlanLineBuildingObjectItem> findAvaliableToTransferItems();
}
