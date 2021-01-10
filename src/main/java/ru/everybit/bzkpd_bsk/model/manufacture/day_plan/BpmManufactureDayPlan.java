package ru.everybit.bzkpd_bsk.model.manufacture.day_plan;

import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bpm_manufacture_day_plan")
public class BpmManufactureDayPlan {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmManufactureDayPlanIdGenerator", sequenceName = "bpm_manufacture_day_plan_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmManufactureDayPlanIdGenerator")
    private Long id;

    @Column(name = "plan_day")
    private Date day;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_month_plan_id")
    private BpmManufactureMonthPlan manufactureMonthPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public BpmManufactureMonthPlan getManufactureMonthPlan() {
        return manufactureMonthPlan;
    }

    public void setManufactureMonthPlan(BpmManufactureMonthPlan manufactureMonthPlan) {
        this.manufactureMonthPlan = manufactureMonthPlan;
    }
}