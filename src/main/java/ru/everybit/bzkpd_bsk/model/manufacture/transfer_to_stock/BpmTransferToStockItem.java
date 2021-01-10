package ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;

@Entity
@Table(name = "BPM_TRANSFER_TO_STOCK_ITEM")
public class BpmTransferToStockItem {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_TRANSFER_TO_STOCK_ITEM_ID_GENERATOR", sequenceName = "BPM_TRANSFER_TO_STOCK_ITEM_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_TRANSFER_TO_STOCK_ITEM_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_transfer_to_stock_id")
    private BpmTransferToStock transferToStock;

    @ManyToOne
    @JoinColumn(name = "bpm_building_plan_item_id")
    private BpmBuildingPlanItem buildingPlanItem;

    @ManyToOne
    @JoinColumn(name = "bpm_manufacture_day_plan_line_building_object_item_id")
    private BpmManufactureDayPlanLineBuildingObjectItem manufactureDayPlanLineBuildingObjectItem;
    
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "approved_quantity")
    private int approvedQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmTransferToStock getTransferToStock() {
        return transferToStock;
    }

    public void setTransferToStock(BpmTransferToStock transferToStock) {
        this.transferToStock = transferToStock;
    }

    public BpmBuildingPlanItem getBuildingPlanItem() {
        return buildingPlanItem;
    }

    public void setBuildingPlanItem(BpmBuildingPlanItem buildingPlanItem) {
        this.buildingPlanItem = buildingPlanItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getApprovedQuantity() {
        return approvedQuantity;
    }

    public void setApprovedQuantity(int approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }

    public BpmManufactureDayPlanLineBuildingObjectItem getManufactureDayPlanLineBuildingObjectItem() {
        return manufactureDayPlanLineBuildingObjectItem;
    }

    public void setManufactureDayPlanLineBuildingObjectItem(
            BpmManufactureDayPlanLineBuildingObjectItem manufactureDayPlanLineBuildingObjectItem) {
        this.manufactureDayPlanLineBuildingObjectItem = manufactureDayPlanLineBuildingObjectItem;
    }
    
    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmTransferToStockItem) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}