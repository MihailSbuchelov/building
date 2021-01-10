package ru.everybit.bzkpd_bsk.model.manufacture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BpmManufactureLineDao extends JpaRepository<BpmManufactureLine, Long> {
    BpmManufactureLine findByName(String name);
}
