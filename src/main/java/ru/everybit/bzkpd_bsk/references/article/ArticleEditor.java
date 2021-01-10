package ru.everybit.bzkpd_bsk.references.article;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.everybit.bzkpd_bsk.application.service.attachment.BpmAttachmentService;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachment;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachmentDao;
import ru.everybit.bzkpd_bsk.application.service.attachment.view.BpmAttachmentComponent;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleKji;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.VerticalLayout;

class ArticleEditor extends BpmWindow {
    private static final long serialVersionUID = 4977726211490236203L;
    
    public static class ArticleImageStartedListener implements Upload.StartedListener {
        private static final long serialVersionUID = 1L;
        public static Set<String> allowedMimeTypes = new HashSet<String>();
        static{
            allowedMimeTypes.add("image/jpeg");
            allowedMimeTypes.add("image/png");
        }
        
        private BpmAttachmentComponent attachmentComponent;
        
        public ArticleImageStartedListener(BpmAttachmentComponent attachmentComponent) {
            this.attachmentComponent = attachmentComponent;
        }
        @Override
        public void uploadStarted(StartedEvent event) {
            if(!allowedMimeTypes.contains(event.getMIMEType())){
                attachmentComponent.getUpload().interruptUpload();
                Notification.show("Ошибка", "Не верный формат файла изображения", Notification.Type.ERROR_MESSAGE);
            }
            
        }
    }
    
    public static class ArticleImageUploadFinishListener implements Upload.FinishedListener {
        private static final long serialVersionUID = 1L;
        private ArticleEditor editor;
        private BpmAttachmentComponent bpmAttachmentComponent;
        public ArticleImageUploadFinishListener(BpmAttachmentComponent bpmAttachmentComponent, ArticleEditor editor) {
            this.bpmAttachmentComponent = bpmAttachmentComponent;
            this.editor = editor;
        }
        @Override
        public void uploadFinished(FinishedEvent event) {
            Long attachmentId = bpmAttachmentComponent.getAttachmentId();
            if(attachmentId != null){
                editor.image.setVisible(true);
                editor.image.setSource(BpmAttachmentService.getAttachmentFileResource(attachmentId));
                editor.getContent().setHeight((int)(Page.getCurrent().getBrowserWindowHeight()*0.97), Sizeable.Unit.PIXELS);
                ((AbstractOrderedLayout)editor.getContent()).setExpandRatio(editor.image, 1);
            }else{
                editor.image.setVisible(true);
            }
            editor.center();
        }
    }
    
    // model
    private BpmArticleType articleType;
    private BpmArticle article;
    // view
    private TextField articleNameTextField;
    private ComboBox articleKjiComboBox;
    private TextField articleVolumeTextField;
    // control
    private ArticleReferenceControl control;
    private BpmAttachmentComponent attachmentComponent;
    private Image image;

    public ArticleEditor(ArticleReferenceControl control, BpmArticleType articleType, BpmArticle article) {
        super(article == null ? "Добавить изделие" : "Изменить изделие");
        this.articleType = articleType;
        this.article = article;
        this.control = control;
        init();
    }

    private void init() {
        articleNameTextField = new TextField("Название изделия");
        articleNameTextField.setWidth("100%");
        articleNameTextField.setInputPrompt("Введите название изделия");

        articleKjiComboBox = new ComboBox("Код обозначения");
        articleKjiComboBox.setWidth("100%");
        articleKjiComboBox.setNullSelectionItemId("Выберите код обозначения");
        List<BpmArticleKji> articleKjiList = BpmDaoFactory.getArticleKjiDao().findAll();
        articleKjiComboBox.setContainerDataSource(new BeanItemContainer<>(BpmArticleKji.class, articleKjiList));
        articleKjiComboBox.setNewItemsAllowed(true);
        articleKjiComboBox.setNewItemHandler(new AbstractSelect.NewItemHandler() {
            private static final long serialVersionUID = 1L;
            @Override
            public void addNewItem(String newItemCaption) {
                BpmArticleKji articleKji = new BpmArticleKji();
                articleKji.setName(newItemCaption);
                articleKji = BpmDaoFactory.getArticleKjiDao().save(articleKji);
                articleKjiComboBox.addItem(articleKji);
                articleKjiComboBox.setValue(articleKji);
                Notification.show("Добавлен новый код обозначения " + newItemCaption, Notification.Type.TRAY_NOTIFICATION);
            }
        });

        articleVolumeTextField = new TextField("Объем м3");
        articleVolumeTextField.setWidth("100%");
        articleVolumeTextField.setInputPrompt("Введите объем изделия");

        if (article != null) {
            articleNameTextField.setValue(article.getName());
            articleKjiComboBox.setValue(article.getArticleKji());
            articleVolumeTextField.setValue(article.getVolume() == null ? "" : String.valueOf(article.getVolume()));
        }
        
        image = new Image();
        image.setHeight("100%");
        image.setVisible(false);
        
        Long attachmentId = null;
        if(article != null && article.getImageAttachment() != null){
            attachmentId = article.getImageAttachment().getId();
            image.setVisible(true);
            image.setSource(BpmAttachmentService.getAttachmentFileResource(attachmentId));
        }
        attachmentComponent = new BpmAttachmentComponent("Изображение", attachmentId);
        HorizontalLayout attachmentComponentLayout = attachmentComponent.buildUi();
        attachmentComponent.getUpload().addStartedListener(new ArticleImageStartedListener(attachmentComponent));
        attachmentComponent.getUpload().addFinishedListener(new ArticleImageUploadFinishListener(attachmentComponent, this));
        
        
        FormLayout formLayout = new FormLayout(articleNameTextField, articleKjiComboBox, articleVolumeTextField);
        formLayout.setWidth("600px");
        Button articleButton = new Button(article == null ? "Добавить" : "Изменить");
        articleButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        articleButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventButtonClick();
            }
        });
        
        VerticalLayout layout = new VerticalLayout(formLayout, image, attachmentComponentLayout, articleButton);
        if(article != null && article.getImageAttachment() != null){
            layout.setHeight((int)(Page.getCurrent().getBrowserWindowHeight()*0.97), Sizeable.Unit.PIXELS);
            layout.setExpandRatio(image, 1);
        }
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setComponentAlignment(articleButton, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private void eventButtonClick() {
        String name = articleNameTextField.getValue();
        if (name == null || "".equals(name)) {
            return;
        }
        BpmArticleKji articleKji = (BpmArticleKji) articleKjiComboBox.getValue();
        if (articleKji == null) {
            return;
        }
        String volume = articleVolumeTextField.getValue();
        if (volume == null || "".equals(volume)) {
            return;
        }

        if (article == null) {
            article = new BpmArticle();
        }
        article.setName(name);
        article.setArticleType(articleType);
        article.setArticleKji(articleKji);
        article.setVolume(Float.valueOf(volume));
        BpmAttachmentDao attachmentDao = BpmAttachmentService.getBpmAttachmentDao();
        BpmAttachment imageAttachment = attachmentDao.findOne(attachmentComponent.getAttachmentId());
        article.setImageAttachment(imageAttachment);
        control.saveArticle(article);

        close();
    }
}
