package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "BPM_BLOCK")
public class BpmBlock {
    @Id
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "BPM_BLOCK_ID_GENERATOR", sequenceName = "BPM_BLOCK_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BLOCK_ID_GENERATOR")
    private Long id;

    @Column(name = "name", length = 2048, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_id")
    private BpmBuildingObject buildingObject;

    @Column(name = "priority")
    private int priority;

    public BpmBlock() {

    }

    public BpmBlock(int priority) {
        setName("");
        setPriority(priority);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BpmBuildingObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BpmBlock block = (BpmBlock) o;

        if (priority != block.priority) return false;
        if (!id.equals(block.id)) return false;
        if (name != null ? !name.equals(block.name) : block.name != null) return false;
        return !(buildingObject != null ? !buildingObject.equals(block.buildingObject) : block.buildingObject != null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }
}
