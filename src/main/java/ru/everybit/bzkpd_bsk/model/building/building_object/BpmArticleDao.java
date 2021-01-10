package ru.everybit.bzkpd_bsk.model.building.building_object;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BpmArticleDao extends PagingAndSortingRepository<BpmArticle, Long> {
    List<BpmArticle> findByArticleTypeAndDeletedOrderByIdAsc(BpmArticleType articleType, boolean deleted);
}
