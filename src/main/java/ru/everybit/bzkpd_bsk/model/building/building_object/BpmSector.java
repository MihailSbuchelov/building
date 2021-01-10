package ru.everybit.bzkpd_bsk.model.building.building_object;

import javax.persistence.*;

@Entity
@Table(name = "bpm_sector")
public class BpmSector {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BPM_SECTOR_ID_GENERATOR", sequenceName = "BPM_SECTOR_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_SECTOR_ID_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_floor_id")
    private BpmFloor floor;

    @ManyToOne
    @JoinColumn(name = "bpm_block_id")
    private BpmBlock block;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpmFloor getFloor() {
        return floor;
    }

    public void setFloor(BpmFloor floor) {
        this.floor = floor;
    }

    public BpmBlock getBlock() {
        return block;
    }

    public void setBlock(BpmBlock block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BpmSector sector = (BpmSector) o;

        if (!id.equals(sector.id)) return false;
        if (floor != null ? !floor.equals(sector.floor) : sector.floor != null) return false;
        return !(block != null ? !block.equals(sector.block) : sector.block != null);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
