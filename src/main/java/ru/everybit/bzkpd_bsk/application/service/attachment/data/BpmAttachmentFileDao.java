package ru.everybit.bzkpd_bsk.application.service.attachment.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmAttachmentFileDao extends PagingAndSortingRepository<BpmAttachmentFile, Long> {
    List<BpmAttachmentFile> findByAttachmentOrderByCreateDateDesc(BpmAttachment attachment);
}
