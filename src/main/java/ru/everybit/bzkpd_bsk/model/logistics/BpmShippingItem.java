package ru.everybit.bzkpd_bsk.model.logistics;

import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;

import javax.persistence.*;

@Entity
@Table(name = "bpm_shipping_item")
public class BpmShippingItem {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmShippingItemIdGenerator", sequenceName = "bpm_shipping_item_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmShippingItemIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_shipping_id")
    private BpmShipping shipping;

    @ManyToOne
    @JoinColumn(name = "bpm_building_plan_item_id")
    private BpmBuildingPlanItem buildingPlanItem;

    @Column(name = "quantity")
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmShipping getShipping() {
        return shipping;
    }

    public void setShipping(BpmShipping shipping) {
        this.shipping = shipping;
    }

    public BpmBuildingPlanItem getBuildingPlanItem() {
        return buildingPlanItem;
    }

    public void setBuildingPlanItem(BpmBuildingPlanItem buildingPlanItem) {
        this.buildingPlanItem = buildingPlanItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmShippingItem) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
