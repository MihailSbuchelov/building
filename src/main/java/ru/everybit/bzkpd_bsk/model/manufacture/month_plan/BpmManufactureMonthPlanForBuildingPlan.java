package ru.everybit.bzkpd_bsk.model.manufacture.month_plan;

import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;

import javax.persistence.*;

@Entity
@Table(name = "bpm_manufacture_month_plan_for_building_plan")
public class BpmManufactureMonthPlanForBuildingPlan {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmManufactureMonthPlanForBuildingPlanIdGenerator", sequenceName = "bpm_manufacture_month_plan_for_building_plan_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmManufactureMonthPlanForBuildingPlanIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_month_plan_id")
    private BpmManufactureMonthPlan manufactureMonthPlan;

    @ManyToOne
    @JoinColumn(name = "bpm_building_plan_id")
    private BpmBuildingPlan buildingPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmManufactureMonthPlan getManufactureMonthPlan() {
        return manufactureMonthPlan;
    }

    public void setManufactureMonthPlan(BpmManufactureMonthPlan manufactureMonthPlan) {
        this.manufactureMonthPlan = manufactureMonthPlan;
    }

    public BpmBuildingPlan getBuildingPlan() {
        return buildingPlan;
    }

    public void setBuildingPlan(BpmBuildingPlan buildingPlan) {
        this.buildingPlan = buildingPlan;
    }
}
