package ru.everybit.bzkpd_bsk.model.building.building_plan;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "BPM_BUILDING_PLAN_REPORT")
public class BpmBuildingPlanReport {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_BUILDING_PLAN_REPORT_ID_GENERATOR", sequenceName = "BPM_BUILDING_PLAN_REPORT_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BUILDING_PLAN_REPORT_ID_GENERATOR")
    private Long id;

    @Column(name = "report_date", nullable = false)
    private Date reportDate;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "bpm_building_plan_report_id", nullable = false)
    private Set<BpmBuildingPlanReportSector> sectors = new LinkedHashSet<BpmBuildingPlanReportSector>();

    @ManyToOne
    @JoinColumn(name = "bpm_building_plan_id")
    private BpmBuildingPlan buildingPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Set<BpmBuildingPlanReportSector> getSectors() {
        return sectors;
    }

    public void setSectors(Set<BpmBuildingPlanReportSector> sectors) {
        this.sectors = sectors;
    }

    public BpmBuildingPlan getBuildingPlan() {
        return buildingPlan;
    }

    public void setBuildingPlan(BpmBuildingPlan buildingPlan) {
        this.buildingPlan = buildingPlan;
    }
}