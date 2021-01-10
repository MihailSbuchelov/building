package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "bpm_building_object_article")
public class BpmBuildingObjectArticle {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "bpm_building_object_article_id_generator", sequenceName = "bpm_building_object_article_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpm_building_object_article_id_generator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_id")
    private BpmBuildingObject buildingObject;

    @ManyToOne
    @JoinColumn(name = "bpm_article_id")
    private BpmArticle article;

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

    public BpmArticle getArticle() {
        return article;
    }

    public void setArticle(BpmArticle article) {
        this.article = article;
    }
}
