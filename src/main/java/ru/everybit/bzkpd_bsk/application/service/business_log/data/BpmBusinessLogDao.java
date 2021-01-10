package ru.everybit.bzkpd_bsk.application.service.business_log.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmBusinessLogDao extends PagingAndSortingRepository<BpmBusinessLog, Integer> {
    List<BpmBusinessLog> findByProcessInstanceIdOrderByMessageTimeAsc(String processInstanceId);
}
