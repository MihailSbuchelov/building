package ru.everybit.bzkpd_bsk.model.building.building_plan;

import javax.persistence.*;

@Entity
@Table(name = "BPM_BUILDING_PLAN_REPORT_SECTOR")
public class BpmBuildingPlanReportSector {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_BUILDING_PLAN_REPORT_SECTOR_ID_GENERATOR", sequenceName = "BPM_BUILDING_PLAN_REPORT_SECTOR_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BUILDING_PLAN_REPORT_SECTOR_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_building_plan_sector_id")
    private BpmBuildingPlanSector sector;

    @ManyToOne
    @JoinColumn(name = "BPM_BUILDING_PLAN_REPORT_ID", insertable = false, updatable = false, nullable = false)
    private BpmBuildingPlanReport report;

    @Column(name = "completion")
    private int completion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public BpmBuildingPlanSector getSector() {
        return sector;
    }

    public void setSector(BpmBuildingPlanSector sector) {
        this.sector = sector;
    }

    public BpmBuildingPlanReport getReport() {
        return report;
    }

    public void setReport(BpmBuildingPlanReport report) {
        this.report = report;
    }

}