package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "BPM_PRODUCT")
public class BpmProduct {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "BPM_PRODUCT_ID_GENERATOR", sequenceName = "BPM_PRODUCT_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_PRODUCT_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_article_id")
    private BpmBuildingObjectArticle buildingObjectArticle;

    @ManyToOne
    @JoinColumn(name = "BPM_SECTOR_ID")
    private BpmSector sector;

    @Column(name = "QUANTITY")
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setSector(BpmSector sector) {
        this.sector = sector;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        this.buildingObjectArticle = buildingObjectArticle;
    }

    public BpmBuildingObjectArticle getBuildingObjectArticle() {
        return buildingObjectArticle;
    }

    public BpmSector getSector() {
        return sector;
    }
}
