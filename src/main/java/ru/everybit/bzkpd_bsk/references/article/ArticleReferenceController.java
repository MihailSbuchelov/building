package ru.everybit.bzkpd_bsk.references.article;

import ru.everybit.bzkpd_bsk.application.view.component.BpmEditButtons;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;

public class ArticleReferenceController {
    public static void init(BpmWorkingArea workingArea) {
        // create
        ArticleReferenceControl control = new ArticleReferenceControl();
        ArticleReferenceView view = new ArticleReferenceView();
        BpmEditButtons articleTypeEditButtons = new BpmEditButtons();
        BpmEditButtons articleEditButtons = new BpmEditButtons();
        ArticleReferenceModel model = new ArticleReferenceModel();
        // init references
        view.setModel(model);
        view.setControl(control);
        view.setArticleTypeEditButtons(articleTypeEditButtons);
        view.setArticleEditButtons(articleEditButtons);
        control.setModel(model);
        control.setView(view);
        control.setArticleTypeEditButtons(articleTypeEditButtons);
        control.setArticleEditButtons(articleEditButtons);
        // init
        model.initModel();
        view.initView();
        control.initControl();
        // init controller
        workingArea.setContent(view.getView());
    }
}
