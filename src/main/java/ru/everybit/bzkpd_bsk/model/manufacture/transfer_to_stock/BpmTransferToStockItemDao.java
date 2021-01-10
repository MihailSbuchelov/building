package ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BpmTransferToStockItemDao extends JpaRepository<BpmTransferToStockItem, Long> {

    public List<BpmTransferToStockItem> findByTransferToStockIdOrderByIdAsc(Long transferId);
}
