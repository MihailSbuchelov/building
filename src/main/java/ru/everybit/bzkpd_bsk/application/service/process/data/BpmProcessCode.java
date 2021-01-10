package ru.everybit.bzkpd_bsk.application.service.process.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bpm_process_code")
public class BpmProcessCode {

    @Id
    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "num")
    private Long num;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BpmProcessCode other = (BpmProcessCode) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (num == null) {
            if (other.num != null)
                return false;
        } else if (!num.equals(other.num))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return code + "-" + num;
    }
}
