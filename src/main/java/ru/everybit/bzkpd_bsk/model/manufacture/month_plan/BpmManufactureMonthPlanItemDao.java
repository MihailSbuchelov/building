package ru.everybit.bzkpd_bsk.model.manufacture.month_plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;

import java.util.List;

public interface BpmManufactureMonthPlanItemDao extends JpaRepository<BpmManufactureMonthPlanItem, Long> {
    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndArticleArticleType(BpmManufactureMonthPlan manufactureMonthPlan, BpmArticleType articleType);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndArticleArticleTypeAndBuildingObject(BpmManufactureMonthPlan manufactureMonthPlan, BpmArticleType articleType, BpmBuildingObject buildingObject);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlan(BpmManufactureMonthPlan manufactureMonthPlan);

//    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndManufactureLine(BpmManufactureMonthPlan manufactureMonthPlan, BpmManufactureLine manufactureLine);

//    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndManufactureLine(BpmManufactureMonthPlan manufactureMonthPlan, BpmManufactureLine manufactureLine);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndArticle(BpmManufactureMonthPlan manufactureMonthPlan, BpmArticle article);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndBuildingObjectAndArticle(BpmManufactureMonthPlan manufactureMonthPlan, BpmBuildingObject buildingObject, BpmArticle article);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanOrderByArticleArticleTypeIdAsc(BpmManufactureMonthPlan manufactureMonthPlan);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndArticleArticleTypeOrderByArticleIdAsc(BpmManufactureMonthPlan manufactureMonthPlan, BpmArticleType articleType);

    List<BpmManufactureMonthPlanItem> findByManufactureMonthPlanAndBuildingObject(BpmManufactureMonthPlan manufactureMonthPlan, BpmBuildingObject buildingObject);
}
