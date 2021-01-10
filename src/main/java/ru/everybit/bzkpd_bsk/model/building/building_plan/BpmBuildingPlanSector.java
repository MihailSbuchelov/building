package ru.everybit.bzkpd_bsk.model.building.building_plan;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBlock;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmFloor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BPM_BUILDING_PLAN_SECTOR")
public class BpmBuildingPlanSector {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_BUILDING_PLAN_SECTOR_ID_GENERATOR", sequenceName = "BPM_BUILDING_PLAN_SECTOR_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BUILDING_PLAN_SECTOR_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BPM_FLOOR_ID")
    private BpmFloor floor;

    @ManyToOne
    @JoinColumn(name = "BPM_BLOCK_ID")
    private BpmBlock block;

    @ManyToOne
    @JoinColumn(name = "BPM_BUILDING_PLAN_ID", insertable = false, updatable = false, nullable = false)
    private BpmBuildingPlan buildingPlan;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "BPM_BUILDING_PLAN_SECTOR_ID", insertable = false, updatable = false, nullable = false)
    private List<BpmBuildingPlanItem> items = new ArrayList<BpmBuildingPlanItem>();

    @Column(name = "completion")
    private int completion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmBuildingPlan getBuildingPlan() {
        return buildingPlan;
    }

    public void setBuildingPlan(BpmBuildingPlan buildingPlan) {
        this.buildingPlan = buildingPlan;
    }

    public BpmFloor getFloor() {
        return floor;
    }

    public void setFloor(BpmFloor floor) {
        this.floor = floor;
    }

    public BpmBlock getBlock() {
        return block;
    }

    public void setBlock(BpmBlock block) {
        this.block = block;
    }

    public List<BpmBuildingPlanItem> getItems() {
        return items;
    }

    public void setItems(List<BpmBuildingPlanItem> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((block == null) ? 0 : block.hashCode());
        result = prime * result
                + ((buildingPlan == null) ? 0 : buildingPlan.hashCode());
        result = prime * result + ((floor == null) ? 0 : floor.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BpmBuildingPlanSector other = (BpmBuildingPlanSector) obj;
        if (block == null) {
            if (other.block != null)
                return false;
        } else if (!block.equals(other.block))
            return false;
        if (buildingPlan == null) {
            if (other.buildingPlan != null)
                return false;
        } else if (!buildingPlan.equals(other.buildingPlan))
            return false;
        if (floor == null) {
            if (other.floor != null)
                return false;
        } else if (!floor.equals(other.floor))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        return true;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

}