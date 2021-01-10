package ru.everybit.bzkpd_bsk.model.building.building_plan;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "BPM_BUILDING_PLAN")
public class BpmBuildingPlan {
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_NEW = "new";

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_BUILDING_PLAN_ID_GENERATOR", sequenceName = "BPM_BUILDING_PLAN_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BUILDING_PLAN_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_id")
    private BpmBuildingObject buildingObject;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "bpm_building_plan_id", nullable = false)
    private Set<BpmBuildingPlanSector> sectors = new LinkedHashSet<BpmBuildingPlanSector>();

    @Column(name = "from_date", nullable = false)
    private Date fromDate;

    @Column(name = "to_date", nullable = false)
    private Date toDate;

    @Column(name = "status", nullable = false)
    private String status = STATUS_NEW;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<BpmBuildingPlanSector> getSectors() {
        return sectors;
    }

    public void setSectors(Set<BpmBuildingPlanSector> sectors) {
        this.sectors = sectors;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BpmBuildingObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}