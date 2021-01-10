package ru.everybit.bzkpd_bsk.model.logistics;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;

import java.util.List;

public interface BpmStockItemDao extends JpaRepository<BpmStockItem, Long> {
    BpmStockItem findByBuildingPlanItem(BpmBuildingPlanItem buildingPlanItem);

    List<BpmStockItem> findByBuildingPlanItemProductBuildingObjectArticleBuildingObject(BpmBuildingObject buildingObject, Sort orders);
}
