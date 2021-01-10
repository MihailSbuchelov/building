package ru.everybit.bzkpd_bsk.model.logistics;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BpmShippingItemDao extends JpaRepository<BpmShippingItem, Long> {
    List<BpmShippingItem> findByShipping(BpmShipping shipping, Sort orders);
}
