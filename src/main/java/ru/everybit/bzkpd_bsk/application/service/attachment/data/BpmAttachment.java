package ru.everybit.bzkpd_bsk.application.service.attachment.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bpm_attachment")
public class BpmAttachment implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BpmAttachmentIdGenerator", sequenceName = "bpm_attachment_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmAttachmentIdGenerator")
    private Long id;

    public Long getId() {
        return id;
    }
}
