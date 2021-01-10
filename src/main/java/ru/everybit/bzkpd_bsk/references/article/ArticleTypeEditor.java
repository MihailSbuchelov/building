package ru.everybit.bzkpd_bsk.references.article;

import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;

class ArticleTypeEditor extends BpmWindow {
    // model
    private BpmArticleType articleType;
    // view
    private TextField articleTypeTextField;
    // control
    private ArticleReferenceControl control;

    public ArticleTypeEditor(ArticleReferenceControl control, BpmArticleType articleType) {
        super(articleType == null ? "Добавить тип изделия" : "Изменить тип изделия");
        this.articleType = articleType;
        this.control = control;
        init();
    }

    private void init() {
        articleTypeTextField = new TextField("Тип изделия");
        articleTypeTextField.setWidth("100%");
        articleTypeTextField.setInputPrompt("Введите название типа изделия");
        if (articleType != null) {
            articleTypeTextField.setValue(articleType.getName());
        }

        Button articleTypeButton = new Button(articleType == null ? "Добавить" : "Изменить");
        articleTypeButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        articleTypeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventButtonClick();
            }
        });

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        formLayout.addComponent(articleTypeTextField);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(formLayout);
        layout.addComponent(articleTypeButton);
        layout.setComponentAlignment(articleTypeButton, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private void eventButtonClick() {
        String value = articleTypeTextField.getValue();
        if (value != null && !"".equals(value)) {
            if (articleType == null) {
                articleType = new BpmArticleType();
            }
            articleType.setName(value);
            control.saveArticleType(articleType);
            close();
        }
    }
}
