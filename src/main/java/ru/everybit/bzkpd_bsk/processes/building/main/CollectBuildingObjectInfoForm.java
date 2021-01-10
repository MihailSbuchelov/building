package ru.everybit.bzkpd_bsk.processes.building.main;

import com.vaadin.ui.TextArea;

import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;

public class CollectBuildingObjectInfoForm extends BpmForm {
    // ui
    private TextArea objectTextArea;
    private TextArea projectOfficeTextArea;
    private TextArea addressTextArea;

    @Override
    public void initUiContent() {
        objectTextArea = new TextArea("Объект");
        objectTextArea.setWidth("600px");
        objectTextArea.setMaxLength(2048);
        objectTextArea.setInputPrompt("Введите информацию об объекте");
        formContentLayout.addComponent(objectTextArea);

        projectOfficeTextArea = new TextArea("Проектное бюро");
        projectOfficeTextArea.setWidth("600px");
        projectOfficeTextArea.setMaxLength(2048);
        projectOfficeTextArea.setInputPrompt("Введите информацию о проектном бюро");
        formContentLayout.addComponent(projectOfficeTextArea);

        addressTextArea = new TextArea("Адрес объекта");
        addressTextArea.setWidth("600px");
        addressTextArea.setMaxLength(2048);
        addressTextArea.setInputPrompt("Введите адрес объекта");
        formContentLayout.addComponent(addressTextArea);
    }

    @Override
    protected void initModel() {
        // Загружаем данные в поля формы
        Long buildingObjectId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        
        if (buildingObjectId != null) {
            BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
            objectTextArea.setValue(buildingObject.getObject());
            projectOfficeTextArea.setValue(buildingObject.getProjectOffice());
            addressTextArea.setValue(buildingObject.getAddress());
        }
        super.initModel();
    }

    @Override
    protected void saveButtonClick() {
        BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().save(getBuildingObjectFromUi());
        Long buildingObjectId = buildingObject.getId();
        setProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID, buildingObjectId);
        
    }

    @Override
    protected void submitButtonClick() {
        BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().save(getBuildingObjectFromUi());
        Long buildingObjectId = buildingObject.getId();
        setProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID, buildingObjectId);
        setProcessVariable(BpmProcessModel.TITLE_VARIABLE, buildingObject.getObject());
        super.submitButtonClick();
    }

    private BpmBuildingObject getBuildingObjectFromUi() {
        Long buildingObjectId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        BpmBuildingObject buildingObject = null;
//        BpmBuildingObject buildingObject = (BpmBuildingObject) getTaskVariable(BUILDING_OBJECT);
        if (buildingObjectId == null) {
            buildingObject = new BpmBuildingObject();
        }else{
            buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
        }

        buildingObject.setObject(objectTextArea.getValue());
        buildingObject.setProjectOffice(projectOfficeTextArea.getValue());
        buildingObject.setAddress(addressTextArea.getValue());
        return buildingObject;
    }
}
