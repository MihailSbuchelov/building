package ru.everybit.bzkpd_bsk.model.building.building_plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import java.util.List;

public interface BpmBuildingPlanDao extends JpaRepository<BpmBuildingPlan, Long> {

    List<BpmBuildingPlan> findByBuildingObject(BpmBuildingObject buildingObject);

    List<BpmBuildingPlan> findByBuildingObjectId(Long buildingObjectId);

    List<BpmBuildingPlan> findByStatusOrderByBuildingObjectObjectAscFromDateAsc(String status);
}
