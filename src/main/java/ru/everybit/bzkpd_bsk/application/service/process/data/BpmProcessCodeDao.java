package ru.everybit.bzkpd_bsk.application.service.process.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BpmProcessCodeDao extends PagingAndSortingRepository<BpmProcessCode, String> {

    BpmProcessCode findByCode(String code);
}
