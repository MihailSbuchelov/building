package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachment;

@Entity
@Table(name = "bpm_article")
public class BpmArticle {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "bpm_article_id_generator", sequenceName = "bpm_article_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpm_article_id_generator")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BpmArticleType articleType;

    @ManyToOne
    @JoinColumn(name = "kji_id")
    private BpmArticleKji articleKji;

    @Column(name = "volume")
    private Float volume;

    @ManyToOne
    @JoinColumn(name = "BPM_IMAGE_ATTACHMENT_ID", nullable = true)
    private BpmAttachment imageAttachment;

    @Column(name = "is_deleted")
    private boolean deleted;

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

    public BpmArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(BpmArticleType articleType) {
        this.articleType = articleType;
    }

    public BpmArticleKji getArticleKji() {
        return articleKji;
    }

    public void setArticleKji(BpmArticleKji articleKji) {
        this.articleKji = articleKji;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmArticle) o).id);
    }

    public BpmAttachment getImageAttachment() {
        return imageAttachment;
    }

    public void setImageAttachment(BpmAttachment imageAttachment) {
        this.imageAttachment = imageAttachment;
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