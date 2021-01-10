package ru.everybit.bzkpd_bsk.processes.building.main;

import java.io.OutputStream;
import java.util.List;

import ru.everybit.bzkpd_bsk.application.service.attachment.BpmAttachmentService;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachment;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachmentDao;
import ru.everybit.bzkpd_bsk.application.service.attachment.view.BpmAttachmentComponent;
import ru.everybit.bzkpd_bsk.application.service.process.BpmProcessService;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmThreeD;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObjectDao;
import ru.everybit.bzkpd_bsk.components.building.main.service.ArticleMergeResult;
import ru.everybit.bzkpd_bsk.components.building.main.service.Check3dModelService;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadBuildingObject3dModelForm extends BpmForm {
    private static final long serialVersionUID = -6133850025888360025L;

    private static final String BUILDING_MODEL_ATTACHMENT_ID = "buildingModelAttachmentId";
    private BpmThreeD bpmThreeD;

    private BeanItemContainer<ArticleMergeResult> mergeContrainer;
    
    
    public UploadBuildingObject3dModelForm() {
        super(false);
    }
    
    public static class BuildingModelUploadReciever extends BpmAttachmentComponent.UploadReciever {
        private static final long serialVersionUID = 8185215658945662372L;

        private String taskId;
        private String variableName;

        public BuildingModelUploadReciever(BpmAttachmentComponent attachmentComponent, String taskId, String variableName) {
            super(attachmentComponent);
            this.taskId = taskId;
            this.variableName = variableName;
        }

        @Override
        public OutputStream receiveUpload(String fileName, String mimeType) {
            Long oldAttachmentId = attachmentComponent.getAttachmentId();
            OutputStream os = super.receiveUpload(fileName, mimeType);
            if (oldAttachmentId == null) {
                BpmProcessService.setTaskVariable(taskId, variableName, attachmentComponent.getAttachmentId());
            }
            return os;
        }
    }

    public static class BuildingModelUploadFinishListener extends BpmAttachmentComponent.UploadFinishedListener {
        private static final long serialVersionUID = 4299539676190326327L;

        private UploadBuildingObject3dModelForm modelForm;

        public BuildingModelUploadFinishListener(BpmAttachmentComponent bpmAttachmentComponent, UploadBuildingObject3dModelForm modelForm) {
            super(bpmAttachmentComponent);
            this.modelForm = modelForm;
        }

        @Override
        public void uploadFinished(FinishedEvent event) {
            super.uploadFinished(event);
            modelForm.refreshThreeDComponent();
            modelForm.refreshModelWarnings();
        }

    }

    @Override
    protected void initUiContent() {
        Long modelAttachmentId = (Long) getTaskVariable(BUILDING_MODEL_ATTACHMENT_ID);
        BpmAttachmentComponent modelAttachmentComponent = new BpmAttachmentComponent("Модель здания", modelAttachmentId);
        modelAttachmentComponent.setUploadReceiver(new BuildingModelUploadReciever(modelAttachmentComponent, task.getId(), BUILDING_MODEL_ATTACHMENT_ID));
        modelAttachmentComponent.setUploadFinishListener(new BuildingModelUploadFinishListener(modelAttachmentComponent, this));
        formContentLayout.addComponent(modelAttachmentComponent.buildUi());

        refreshThreeDComponent();
        refreshModelWarnings();
    }

    private void refreshModelWarnings() {
        if(mergeContrainer == null)
            return;
        mergeContrainer.removeAllItems();
        Long buildingObjectId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        Long modelAttachmentId = (Long) getTaskVariable(BUILDING_MODEL_ATTACHMENT_ID);
        List<ArticleMergeResult> mergeArticles = Check3dModelService.mergeArticles(buildingObjectId, modelAttachmentId);
        mergeContrainer.addAll(mergeArticles);
    }

    private void refreshThreeDComponent() {
        Long modelAttachmentId = (Long) getTaskVariable(BUILDING_MODEL_ATTACHMENT_ID);
        if (modelAttachmentId != null) {
            if (bpmThreeD == null) {
                Long buildingObjectId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
                bpmThreeD = new BpmThreeD("preview3d", buildingObjectId);
                formContentLayout.addComponent(bpmThreeD);
            }
            bpmThreeD.loadScene(modelAttachmentId);
            
            
            if(mergeContrainer == null ){
                HorizontalLayout mergeHeaderLayout = new HorizontalLayout();
                mergeHeaderLayout.setWidth("100%");
                Label mergeLabel = new Label("Ошибки связи модели и комплектовочной ведомости");
                mergeLabel.addStyleName(BpmThemeStyles.LABEL_H2);
                mergeHeaderLayout.addComponent(mergeLabel);
                
                CheckBox onlyErrorFilter = new CheckBox("Показать только ошибки");
                onlyErrorFilter.addValueChangeListener(new Property.ValueChangeListener() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        mergeContrainer.removeContainerFilters("hasError");
                        if( (Boolean) event.getProperty().getValue() ){
                            mergeContrainer.addContainerFilter(new Compare.Equal("hasError", true));
                        }
                    }
                });
                mergeHeaderLayout.addComponent(onlyErrorFilter);
                mergeHeaderLayout.setComponentAlignment(onlyErrorFilter, Alignment.MIDDLE_RIGHT);
                
                formContentLayout.addComponent(mergeHeaderLayout);
                
                Grid mergeGrid = new Grid();
                mergeGrid.setWidth("100%");
                mergeGrid.setSelectionMode(SelectionMode.NONE);
                
                mergeGrid.setColumns("dbName", "dbFloor", "dbBlock", "dbType", "modelName", "modelFloor", "modelBlock", "modelType");
                mergeGrid.getColumn("dbName").setHeaderCaption("Наименование изделия").setWidth(120d);;
                mergeGrid.getColumn("modelName").setHeaderCaption("Наименование изделия").setWidth(120d);;
                mergeGrid.getColumn("dbFloor").setHeaderCaption("Уровень").setWidth(100d);
                mergeGrid.getColumn("modelFloor").setHeaderCaption("Уровень").setWidth(100d);
                mergeGrid.getColumn("dbBlock").setHeaderCaption("Блок-секция").setWidth(90d);
                mergeGrid.getColumn("modelBlock").setHeaderCaption("Блок-секция").setWidth(90d);
                mergeGrid.getColumn("dbType").setHeaderCaption("Тип изделия");
                mergeGrid.getColumn("modelType").setHeaderCaption("Тип изделия");
                
                HeaderRow prependHeaderRow = mergeGrid.prependHeaderRow();
                
                prependHeaderRow.join("modelFloor", "modelBlock", "modelType", "modelName").setText("3D Модель");
                prependHeaderRow.join("dbFloor", "dbBlock", "dbType", "dbName").setText("Комплектовочная ведомость");
                
                HeaderRow filterHeaderRow = mergeGrid.appendHeaderRow();
                
                mergeContrainer = new BeanItemContainer<>(ArticleMergeResult.class);
                for (final Object pid : mergeContrainer.getContainerPropertyIds()) {
                    HeaderCell cell = filterHeaderRow.getCell(pid);
                    if(cell == null)
                        continue;
                    
                    TextField filterField = new TextField();
                    filterField.setWidth("100%");
                    filterField.addStyleName(ValoTheme.TEXTFIELD_TINY);
                    filterField.addTextChangeListener(new FieldEvents.TextChangeListener() {
                        private static final long serialVersionUID = 1L;
                        @Override
                        public void textChange(TextChangeEvent event) {
                            mergeContrainer.removeContainerFilters(pid);
                            String value = event.getText();
                            if (!value.isEmpty()) {
                                mergeContrainer.addContainerFilter(new SimpleStringFilter(pid, value, true, false));
                            }
                        }
                    });
                    cell.setStyleName("filter-header");
                    cell.setComponent(filterField);
                }

                
                mergeGrid.setCellStyleGenerator(new Grid.CellStyleGenerator() {
                    private static final long serialVersionUID = 1L;
    
                    @Override
                    public String getStyle(CellReference cellReference) {
                        BeanItem<ArticleMergeResult> beanItem = mergeContrainer.getItem(cellReference.getItemId());
                        ArticleMergeResult bean = beanItem.getBean();
                        
                        Object propertyId = cellReference.getPropertyId();
//                        
                        if("modelFloor".equals(propertyId)){
                            if(bean.getDbFloor() == null)
                                return "highlight-yellow";
                            if(!bean.matchFloor())
                                return "highlight-red";
                        }
                        
                        if("modelBlock".equals(propertyId)){
                            if(bean.getDbBlock() == null)
                                return "highlight-yellow";
                            if(!bean.matchBlock())
                                return "highlight-red";
                        }
                        
                        if("modelType".equals(propertyId)){
                            if(bean.getDbType() == null)
                                return "highlight-yellow";
                            if(!bean.matchType())
                                return "highlight-red";
                        }
                        
                        if("modelName".equals(propertyId)){
                            if(bean.getDbName() == null)
                                return "highlight-yellow";
                            if(!bean.matchName())
                                return "highlight-red";
                        }
                        
                        return null;
                    }
                });
                mergeGrid.setContainerDataSource(mergeContrainer);
                formContentLayout.addComponent(mergeGrid);
            }
        }
    }

    
    @Override
    protected void submitButtonClick() {
        Long modelAttachmentId = (Long) getTaskVariable(BUILDING_MODEL_ATTACHMENT_ID);
        BpmAttachmentDao attachmentDao = BpmAttachmentService.getBpmAttachmentDao();
        BpmAttachment modelAttachment = attachmentDao.findOne(modelAttachmentId);

        Long buildingObjectId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);

        BpmBuildingObjectDao buildingObjectDao = BpmDaoFactory.getBuildingObjectDao();
        BpmBuildingObject buildingObject = buildingObjectDao.findOne(buildingObjectId);
        buildingObject.setModelAttachment(modelAttachment);
        buildingObjectDao.save(buildingObject);

        super.submitButtonClick();
    }
}
