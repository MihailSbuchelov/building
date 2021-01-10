package ru.everybit.bzkpd_bsk.model.manufacture.day_plan;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import javax.persistence.*;

@Entity
@Table(name = "bpm_manufacture_day_plan_line_building_object_item")
public class BpmManufactureDayPlanLineBuildingObjectItem {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmManufactureDayPlanLineBuildingObjectItemIdGenerator", sequenceName = "bpm_manufacture_day_plan_line_building_object_item_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmManufactureDayPlanLineBuildingObjectItemIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_day_plan_line_id")
    private BpmManufactureDayPlanLine manufactureDayPlanLine;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_id")
    private BpmBuildingObject buildingObject;

    @ManyToOne
    @JoinColumn(name = "bpm_article_id")
    private BpmArticle article;

    @Column(name = "plan_quantity")
    private int planQuantity;

    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "stock_quantity")
    private int stockQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmManufactureDayPlanLine getManufactureDayPlanLine() {
        return manufactureDayPlanLine;
    }

    public void setManufactureDayPlanLine(BpmManufactureDayPlanLine manufactureDayPlanLine) {
        this.manufactureDayPlanLine = manufactureDayPlanLine;
    }

    public BpmBuildingObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    public BpmArticle getArticle() {
        return article;
    }

    public void setArticle(BpmArticle article) {
        this.article = article;
    }

    public int getPlanQuantity() {
        return planQuantity;
    }

    public void setPlanQuantity(int planQuantity) {
        this.planQuantity = planQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmManufactureDayPlanLineBuildingObjectItem) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}