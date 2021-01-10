package ru.everybit.bzkpd_bsk.model.logistics;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bpm_shipping")
public class BpmShipping {
    public static String STATUS_NEW = "Новый";
    public static String STATUS_IN_PROGRESS = "В пути";
    public static String STATUS_DONE = "Завершен";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "BpmShippingIdGenerator", sequenceName = "bpm_shipping_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmShippingIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpm_building_object_id")
    private BpmBuildingObject buildingObject;

    @Column(name = "shipping_number")
    private Integer shippingNumber;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private String status;

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

    public Integer getShippingNumber() {
        return shippingNumber;
    }

    public void setShippingNumber(Integer shippingNumber) {
        this.shippingNumber = shippingNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmShipping) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
