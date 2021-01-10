package ru.everybit.bzkpd_bsk.references.article;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmEditButtons;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;

import java.util.List;

class ArticleReferenceView implements BpmView {
    // model
    private ArticleReferenceModel model;
    // view
    private BpmComboBox articleTypeComboBox;
    private BpmEditButtons articleTypeEditButtons;
    private BpmGrid articleGrid;
    private BpmEditButtons articleEditButtons;
    private VerticalLayout layout;
    // control
    private ArticleReferenceControl control;

    ArticleReferenceView() {
        articleTypeComboBox = new BpmComboBox();
        layout = new VerticalLayout();
    }

    public void setModel(ArticleReferenceModel model) {
        this.model = model;
    }

    public void setControl(ArticleReferenceControl control) {
        this.control = control;
    }

    public void setArticleTypeEditButtons(BpmEditButtons articleTypeEditButtons) {
        this.articleTypeEditButtons = articleTypeEditButtons;
    }

    public void setArticleEditButtons(BpmEditButtons articleEditButtons) {
        this.articleEditButtons = articleEditButtons;
    }

    @Override
    public void initView() {
        initArticleTypeComboBox();
        initArticleTypeEditButtons();
        initArticleGrid();
        initArticleEditButtons();
        initLayout();
    }

    private void initArticleTypeComboBox() {
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.setNullSelectionAllowed(false);
        articleTypeComboBox.setTextInputAllowed(false);
        articleTypeComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                control.eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
            }
        });
    }

    private void initArticleTypeEditButtons() {
        articleTypeEditButtons.addButtonStyleName(BpmThemeStyles.BUTTON_SMALL);
        articleTypeEditButtons.addButtonStyleName(BpmThemeStyles.BUTTON_BORDERLESS);
    }

    private void initArticleGrid() {
        articleGrid = new BpmGrid();
        articleGrid.addColumn("articleId", Long.class).setHidden(true);
        articleGrid.addColumn("articleName").setHeaderCaption("Наименование изделия").setSortable(false);
        articleGrid.addColumn("articleKji").setHeaderCaption("Код обозначения").setSortable(false);
        articleGrid.addColumn("volume").setHeaderCaption("Объем м3").setSortable(false);
        Grid.HeaderRow prependHeaderRow = articleGrid.prependHeaderRow();
        Grid.HeaderCell cell = prependHeaderRow.join("articleName", "articleKji", "volume");
        cell.setComponent(createArticleLayout());
        articleGrid.setWidth("100%");
        articleGrid.setEditorEnabled(false);
        articleGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                control.eventSelectArticle((BpmArticle) articleGrid.getSelectedRow());
            }
        });
    }

    private HorizontalLayout createArticleLayout() {
        HorizontalLayout articleTypeLayout = new HorizontalLayout();
        articleTypeLayout.setWidth("100%");
        articleTypeLayout.addComponent(articleTypeComboBox);
        articleTypeLayout.setExpandRatio(articleTypeComboBox, 1);
        articleTypeLayout.addComponent(articleTypeEditButtons);
        articleTypeLayout.setComponentAlignment(articleTypeEditButtons, Alignment.MIDDLE_CENTER);
        return articleTypeLayout;
    }

    private void initArticleEditButtons() {
        articleEditButtons.setSpacing(true);
    }

    private void initLayout() {
        Label titleLabel = new Label("Справочник номенклатуры изделий");
        titleLabel.addStyleName(BpmThemeStyles.LABEL_H2);

        layout.setSpacing(true);
        layout.addComponent(titleLabel);
        layout.addComponent(articleGrid);
        layout.addComponent(articleEditButtons);
    }

    @Override
    public Component getView() {
        return layout;
    }

    public void initArticleTypeListAndSelectFirstItem() {
        List<BpmArticleType> articleTypeList = model.getArticleTypeList();
        articleTypeComboBox.setContainerDataSource(new BeanItemContainer<>(BpmArticleType.class, articleTypeList));
        articleTypeComboBox.selectFirstItem();
    }

    public void eventUpdateRows() {
        List<BpmArticle> articleList = model.getArticleList();

        articleGrid.getContainerDataSource().removeAllItems();
        Container.Indexed container = articleGrid.getContainerDataSource();
        for (BpmArticle article : articleList) {
            addArticleToContainer(container, article);
        }
    }

    public void addArticleType(BpmArticleType articleType) {
        articleTypeComboBox.getContainerDataSource().addItem(articleType);
        articleTypeComboBox.setValue(articleType);
    }

    public void updateArticleTypes() {
        articleTypeComboBox.markAsDirty();
    }

    public void addArticle(BpmArticle article) {
        addArticleToContainer(articleGrid.getContainerDataSource(), article);
    }

    public void updateArticle(BpmArticle article) {
        Item item = articleGrid.getContainerDataSource().getItem(article);
        updateArticle(item, article);
    }

    private void addArticleToContainer(Container.Indexed container, BpmArticle article) {
        Item item = container.addItem(article);
        updateArticle(item, article);
    }

    @SuppressWarnings("unchecked")
    private void updateArticle(Item item, BpmArticle article) {
        item.getItemProperty("articleId").setValue(article.getId());
        item.getItemProperty("articleName").setValue(article.getName());
        item.getItemProperty("articleKji").setValue(article.getArticleKji().getName());
        item.getItemProperty("volume").setValue(article.getVolume() == null ? "" : String.valueOf(article.getVolume()));
    }
}
