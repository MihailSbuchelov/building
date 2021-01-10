package ru.everybit.bzkpd_bsk.application.service.business_log.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bpm_business_log")
public class BpmBusinessLog {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BpmBusinessLogIdGenerator", sequenceName = "bpm_business_log_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BpmBusinessLogIdGenerator")
    private Long id;

    @Column(name = "process_instance_id", length = 64, nullable = false)
    private String processInstanceId;

    @Column(name = "message_time", nullable = false)
    private Date messageTime;

    @Column(name = "message", length = -1, nullable = false)
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BpmBusinessLog that = (BpmBusinessLog) o;
        return id.equals(that.id) &&
                message.equals(that.message) &&
                messageTime.equals(that.messageTime) &&
                processInstanceId.equals(that.processInstanceId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + processInstanceId.hashCode();
        result = 31 * result + messageTime.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
