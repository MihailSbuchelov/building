package ru.everybit.bzkpd_bsk.references.article;

import ru.everybit.bzkpd_bsk.application.view.component.BpmControl;
import ru.everybit.bzkpd_bsk.application.view.component.BpmEditButtons;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;

class ArticleReferenceControl implements BpmControl {
    // model
    private ArticleReferenceModel model;
    // view
    private ArticleReferenceView view;
    private BpmEditButtons articleTypeEditButtons;
    private BpmEditButtons articleEditButtons;

    public void setModel(ArticleReferenceModel model) {
        this.model = model;
    }

    public void setView(ArticleReferenceView view) {
        this.view = view;
    }

    public void setArticleTypeEditButtons(BpmEditButtons articleTypeEditButtons) {
        this.articleTypeEditButtons = articleTypeEditButtons;
    }

    public void setArticleEditButtons(BpmEditButtons articleEditButtons) {
        this.articleEditButtons = articleEditButtons;
    }

    @Override
    public void initControl() {
        articleTypeEditButtons.addAddListener(new BpmEditButtons.AddListener() {
            @Override
            public void eventAdd() {
                eventAddArticleType();
            }
        });
        articleTypeEditButtons.addDeleteListener(new BpmEditButtons.DeleteListener() {
            @Override
            public void eventDelete(Object item) {
                eventDeleteArticleType((BpmArticleType) item);
            }
        });
        articleTypeEditButtons.addEditListener(new BpmEditButtons.EditListener() {
            @Override
            public void eventEdit(Object item) {
                eventEditArticleType((BpmArticleType) item);
            }
        });

        articleEditButtons.addAddListener(new BpmEditButtons.AddListener() {
            @Override
            public void eventAdd() {
                eventAddArticle();
            }
        });
        articleEditButtons.addDeleteListener(new BpmEditButtons.DeleteListener() {
            @Override
            public void eventDelete(Object item) {
                eventDeleteArticle((BpmArticle) item);
            }
        });
        articleEditButtons.addEditListener(new BpmEditButtons.EditListener() {
            @Override
            public void eventEdit(Object item) {
                eventEditArticle((BpmArticle) item);
            }
        });

        view.initArticleTypeListAndSelectFirstItem();
    }

    void eventArticleTypeSelected(BpmArticleType articleType) {
        model.setArticleType(articleType);
        articleTypeEditButtons.selectItem(articleType);
        view.eventUpdateRows();
    }

    void eventAddArticleType() {
        new ArticleTypeEditor(this, null).show();
    }

    void eventEditArticleType(BpmArticleType articleType) {
        new ArticleTypeEditor(this, articleType).show();
    }

    void eventDeleteArticleType(BpmArticleType articleType) {
        model.removeArticleType(articleType);
        view.initArticleTypeListAndSelectFirstItem();
    }

    void eventAddArticle() {
        new ArticleEditor(this, model.getArticleType(), null).show();
    }

    void eventSelectArticle(BpmArticle article) {
        articleEditButtons.selectItem(article);
    }

    void eventEditArticle(BpmArticle article) {
        new ArticleEditor(this, model.getArticleType(), article).show();
    }

    void eventDeleteArticle(BpmArticle article) {
        model.removeArticle(article);
        view.eventUpdateRows();
    }

    void saveArticleType(BpmArticleType articleType) {
        boolean add = articleType.getId() == null;
        articleType = model.saveArticleType(articleType);
        if (add) {
            view.addArticleType(articleType);
        } else {
            view.updateArticleTypes();
        }
    }

    void saveArticle(BpmArticle article) {
        boolean add = article.getId() == null;
        article = model.saveArticle(article);
        if (add) {
            view.addArticle(article);
        } else {
            view.updateArticle(article);
        }
    }
}
