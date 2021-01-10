package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "bpm_article_type")
public class BpmArticleType {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "bpm_article_type_id_generator", sequenceName = "bpm_article_type_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpm_article_type_id_generator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Transient
    private Integer totalProductQuantity;

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getTotalProductQuantity() {
        return totalProductQuantity;
    }

    public void setTotalProductQuantity(Integer totalProductQuantity) {
        this.totalProductQuantity = totalProductQuantity;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmArticleType) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return totalProductQuantity == null ? getName() : getName() + " (" + totalProductQuantity + ")";
    }
}
