package ru.everybit.bzkpd_bsk.model.manufacture;

import javax.persistence.*;

@Entity
@Table(name = "BPM_MANUFACTURE_LINE")
public class BpmManufactureLine {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_MANUFACTURE_LINE_ID_GENERATOR", sequenceName = "BPM_MANUFACTURE_LINE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_MANUFACTURE_LINE_ID_GENERATOR")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "month_max_volume")
    private Float monthMaxVolume;

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

    public Float getMonthMaxVolume() {
        return monthMaxVolume;
    }

    public void setMonthMaxVolume(Float monthMaxVolume) {
        this.monthMaxVolume = monthMaxVolume;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmManufactureLine) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return getName();
    }
}