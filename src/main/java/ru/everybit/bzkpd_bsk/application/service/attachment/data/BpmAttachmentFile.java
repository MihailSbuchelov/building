package ru.everybit.bzkpd_bsk.application.service.attachment.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bpm_attachment_file")
public class BpmAttachmentFile {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BpmAttachmentFileIdGenerator", sequenceName = "bpm_attachment_file_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmAttachmentFileIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attachment_id")
    private BpmAttachment attachment;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    public Long getId() {
        return id;
    }

    public BpmAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(BpmAttachment attachment) {
        this.attachment = attachment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
