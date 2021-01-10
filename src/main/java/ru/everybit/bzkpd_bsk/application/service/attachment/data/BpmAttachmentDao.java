package ru.everybit.bzkpd_bsk.application.service.attachment.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BpmAttachmentDao extends PagingAndSortingRepository<BpmAttachment, Long> {
}
