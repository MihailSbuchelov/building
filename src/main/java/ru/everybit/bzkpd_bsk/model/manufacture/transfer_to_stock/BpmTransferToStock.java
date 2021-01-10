package ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "BPM_TRANSFER_TO_STOCK")
public class BpmTransferToStock {
    public static final String STATUS_NEW = "Создана";
    public static final String STATUS_IN_PROGRESS = "В работе";
    public static final String STATUS_DONE = "Завершена";
    
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "BPM_TRANSFER_TO_STOCK_ID_GENERATOR", sequenceName = "BPM_TRANSFER_TO_STOCK_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPM_TRANSFER_TO_STOCK_ID_GENERATOR")
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && id.equals(((BpmTransferToStock) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}