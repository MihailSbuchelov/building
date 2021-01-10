package ru.everybit.bzkpd_bsk.model.manufacture.month_plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;

import java.util.List;

public interface BpmManufactureMonthPlanForBuildingPlanDao extends JpaRepository<BpmManufactureMonthPlanForBuildingPlan, Long> {
    List<BpmManufactureMonthPlanForBuildingPlan> findByManufactureMonthPlan(BpmManufactureMonthPlan manufactureMonthPlan);

    List<BpmManufactureMonthPlanForBuildingPlan> findByManufactureMonthPlanOrderByBuildingPlanBuildingObjectObjectAsc(BpmManufactureMonthPlan manufactureMonthPlan);

    List<BpmManufactureMonthPlanForBuildingPlan> findByBuildingPlan(BpmBuildingPlan buildingPlan);
}
