package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "bpm_article_kji")
public class BpmArticleKji {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "bpm_article_kji_id_generator", sequenceName = "bpm_article_kji_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpm_article_kji_id_generator")
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmArticleKji) o).id);
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