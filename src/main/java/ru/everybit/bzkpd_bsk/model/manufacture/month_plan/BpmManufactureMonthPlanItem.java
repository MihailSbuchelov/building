package ru.everybit.bzkpd_bsk.model.manufacture.month_plan;

import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import javax.persistence.*;

@Entity
@Table(name = "bpm_manufacture_month_plan_item")
public class BpmManufactureMonthPlanItem {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmManufacturePlanItemIdGenerator", sequenceName = "bpm_manufacture_month_plan_item_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmManufacturePlanItemIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_month_plan_id")
    private BpmManufactureMonthPlan manufactureMonthPlan;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_id")
    private BpmBuildingObject buildingObject;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_line_id")
    private BpmManufactureLine manufactureLine;

    @ManyToOne
    @JoinColumn(name = "bpm_article_id")
    private BpmArticle article;

    @Column(name = "quantity")
    private Integer quantity;

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

    public BpmBuildingObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    public BpmManufactureLine getManufactureLine() {
        return manufactureLine;
    }

    public void setManufactureLine(BpmManufactureLine manufactureLine) {
        this.manufactureLine = manufactureLine;
    }

    public BpmArticle getArticle() {
        return article;
    }

    public void setArticle(BpmArticle article) {
        this.article = article;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}