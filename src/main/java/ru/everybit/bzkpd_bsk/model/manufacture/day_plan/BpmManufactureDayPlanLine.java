package ru.everybit.bzkpd_bsk.model.manufacture.day_plan;

import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;

import javax.persistence.*;

@Entity
@Table(name = "bpm_manufacture_day_plan_line")
public class BpmManufactureDayPlanLine {
    public static final String STATUS_NEW = "Создана";
    public static final String STATUS_IN_PROGRESS = "В работе";
    public static final String STATUS_DONE = "Завершена";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmManufactureDayPlanLineIdGenerator", sequenceName = "bpm_manufacture_day_plan_line_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmManufactureDayPlanLineIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_day_plan_id")
    private BpmManufactureDayPlan manufactureDayPlan;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_line_id")
    private BpmManufactureLine manufactureLine;

    @Column(name = "manager_comment")
    private String managerComment;

    @Column(name = "employee_comment")
    private String employeeComment;

    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmManufactureDayPlan getManufactureDayPlan() {
        return manufactureDayPlan;
    }

    public void setManufactureDayPlan(BpmManufactureDayPlan manufactureDayPlan) {
        this.manufactureDayPlan = manufactureDayPlan;
    }

    public BpmManufactureLine getManufactureLine() {
        return manufactureLine;
    }

    public void setManufactureLine(BpmManufactureLine manufactureLine) {
        this.manufactureLine = manufactureLine;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getEmployeeComment() {
        return employeeComment;
    }

    public void setEmployeeComment(String employeeComment) {
        this.employeeComment = employeeComment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmManufactureDayPlanLine) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return manufactureLine.getName();
    }
}