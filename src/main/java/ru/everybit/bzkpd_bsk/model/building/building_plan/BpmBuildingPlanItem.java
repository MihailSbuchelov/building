package ru.everybit.bzkpd_bsk.model.building.building_plan;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmProduct;

import javax.persistence.*;

@Entity
@Table(name = "BPM_BUILDING_PLAN_ITEM")
public class BpmBuildingPlanItem {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BPM_BUILDING_PLAN_ITEM_ID_GENERATOR", sequenceName = "BPM_BUILDING_PLAN_ITEM_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BUILDING_PLAN_ITEM_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BPM_PRODUCT_ID")
    private BpmProduct product;

    @ManyToOne
    @JoinColumn(name = "BPM_BUILDING_PLAN_SECTOR_ID", insertable = false, updatable = false)
    private BpmBuildingPlanSector buildingPlanSector;

    // Планируется поставить
    @Column(name = "QUANTITY")
    private int quantity;

    // Запланировано на производстве
    @Column(name = "production_reserve_quantity")
    private int productionReserveQuantity;
    
    // В производстве
    @Column(name = "PRODUCTION_QUANTITY")
    private int productionQuantity;

    // На складе
    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    // Но лок. складе
    @Column(name = "LOCAL_STOCK_QUANTITY")
    private int localStockQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmProduct getProduct() {
        return product;
    }

    public void setProduct(BpmProduct product) {
        this.product = product;
    }

    public BpmBuildingPlanSector getBuildingPlanSector() {
        return buildingPlanSector;
    }

    public void setBuildingPlanSector(BpmBuildingPlanSector buildingPlanSector) {
        this.buildingPlanSector = buildingPlanSector;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductionReserveQuantity() {
        return productionReserveQuantity;
    }

    public void setProductionReserveQuantity(int productionReserveQuantity) {
        this.productionReserveQuantity = productionReserveQuantity;
    }

    public int getProductionQuantity() {
        return productionQuantity;
    }

    public void setProductionQuantity(int productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getLocalStockQuantity() {
        return localStockQuantity;
    }

    public void setLocalStockQuantity(int localStockQuantity) {
        this.localStockQuantity = localStockQuantity;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmBuildingPlanItem) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}