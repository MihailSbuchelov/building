package ru.everybit.bzkpd_bsk.references.article;

import ru.everybit.bzkpd_bsk.application.view.component.BpmModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;

import java.util.List;

class ArticleReferenceModel implements BpmModel {
    // model
    private BpmArticleType articleType;

    @Override
    public void initModel() {
    }

    public BpmArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(BpmArticleType articleType) {
        this.articleType = articleType;
    }

    public List<BpmArticleType> getArticleTypeList() {
        return BpmDaoFactory.getArticleTypeDao().findByDeletedOrderByIdAsc(false);
    }

    public BpmArticleType saveArticleType(BpmArticleType articleType) {
        articleType = BpmDaoFactory.getArticleTypeDao().save(articleType);
        setArticleType(articleType);
        return articleType;
    }

    public void removeArticleType(BpmArticleType articleType) {
        List<BpmArticle> articleList = getArticleList();
        for (BpmArticle article : articleList) {
            removeArticle(article);
        }
        articleType.setDeleted(true);
        BpmDaoFactory.getArticleTypeDao().save(articleType);
        this.articleType = null;
    }

    public List<BpmArticle> getArticleList() {
        return BpmDaoFactory.getArticleDao().findByArticleTypeAndDeletedOrderByIdAsc(articleType, false);
    }

    public BpmArticle saveArticle(BpmArticle article) {
        return BpmDaoFactory.getArticleDao().save(article);
    }

    void removeArticle(BpmArticle article) {
        article.setDeleted(true);
        BpmDaoFactory.getArticleDao().save(article);
    }
}
