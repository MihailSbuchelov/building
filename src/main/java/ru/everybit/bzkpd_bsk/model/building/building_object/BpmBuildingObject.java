package ru.everybit.bzkpd_bsk.model.building.building_object;

import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachment;

import javax.persistence.*;

@Entity
@Table(name = "BPM_BUILDING_OBJECT")
public class BpmBuildingObject {
    @Id
    @SequenceGenerator(name = "BPM_BUILDING_OBJECT_GENERATOR", sequenceName = "BPM_BUILDING_OBJECT_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_BUILDING_OBJECT_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "object", length = 2048)
    private String object;

    @Column(name = "project_office", length = 2048)
    private String projectOffice;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "BPM_MODEL_ATTACHMENT_ID", nullable = true)
    private BpmAttachment modelAttachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getProjectOffice() {
        return projectOffice;
    }

    public void setProjectOffice(String projectOffice) {
        this.projectOffice = projectOffice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BpmAttachment getModelAttachment() {
        return modelAttachment;
    }

    public void setModelAttachment(BpmAttachment modelAttachment) {
        this.modelAttachment = modelAttachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BpmBuildingObject that = (BpmBuildingObject) o;

        if (!id.equals(that.id)) return false;
        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        if (projectOffice != null ? !projectOffice.equals(that.projectOffice) : that.projectOffice != null)
            return false;
        return !(address != null ? !address.equals(that.address) : that.address != null);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getObject();
    }
}
