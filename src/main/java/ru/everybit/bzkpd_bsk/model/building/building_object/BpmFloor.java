package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "BPM_FLOOR")
public class BpmFloor {
    @Id
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "BPM_FLOOR_ID_GENERATOR", sequenceName = "BPM_FLOOR_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_FLOOR_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BPM_BUILDING_OBJECT_ID")
    private BpmBuildingObject buildingObject;

    @Column(name = "name")
    private String name;

    @Column(name = "priority")
    private int priority;

    @Column(name = "is_lower_floor")
    private boolean lowerFloor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmBuildingObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isLowerFloor() {
        return lowerFloor;
    }

    public void setLowerFloor(boolean lowerFloor) {
        this.lowerFloor = lowerFloor;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmFloor) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
