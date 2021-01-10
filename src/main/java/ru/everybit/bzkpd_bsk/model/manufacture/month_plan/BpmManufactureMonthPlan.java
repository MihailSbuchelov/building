package ru.everybit.bzkpd_bsk.model.manufacture.month_plan;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "bpm_manufacture_month_plan")
public class BpmManufactureMonthPlan {
    public static final String STATUS_NEW = "Новый";
    public static final String STATUS_IN_PROGRESS = "В работе";
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmManufacturePlanIdGenerator", sequenceName = "bpm_manufacture_month_plan_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmManufacturePlanIdGenerator")
    private Long id;

    @Column(name = "plan_month")
    private Date month;

    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmManufactureMonthPlan) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy.MM").format(month);
    }
}