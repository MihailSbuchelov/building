package ru.everybit.bzkpd_bsk.model.manufacture.day_plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BpmManufactureDayPlanLineDao extends JpaRepository<BpmManufactureDayPlanLine, Long> {
    List<BpmManufactureDayPlanLine> findByManufactureDayPlanOrderByManufactureLineNameAsc(BpmManufactureDayPlan manufactureDayPlan);
}
