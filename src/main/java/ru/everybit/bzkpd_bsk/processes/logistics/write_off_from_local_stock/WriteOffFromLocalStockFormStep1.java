package ru.everybit.bzkpd_bsk.processes.logistics.write_off_from_local_stock;

import com.vaadin.ui.FormLayout;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

public class WriteOffFromLocalStockFormStep1 extends BpmWizardFormStep {
    // view
    private BpmComboBox buildingObjectComboBox;

    public WriteOffFromLocalStockFormStep1(BpmWizardForm wizardForm) {
        super(WriteOffFromLocalStockFormStep1.class.getSimpleName(), wizardForm);
    }

    @Override
    public void init() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        wizardFormStepLayout.addComponent(formLayout);

        buildingObjectComboBox = new BpmComboBox("Объект");
        buildingObjectComboBox.setWidth("100%");
        buildingObjectComboBox.setNullSelectionAllowed(false);
        buildingObjectComboBox.setTextInputAllowed(false);
        for (BpmBuildingObject buildingObject : BpmDaoFactory.getBuildingObjectDao().findAll(new Sort(Sort.Direction.ASC, "object"))) {
            buildingObjectComboBox.addItem(buildingObject);
        }
        buildingObjectComboBox.selectFirstItem();
        formLayout.addComponent(buildingObjectComboBox);
    }

    @Override
    public void eventNextButtonClick() {
        BpmBuildingObject buildingObject = (BpmBuildingObject) buildingObjectComboBox.getValue();
        getForm().setTaskVariable("buildingObjectId", buildingObject.getId());
    }
}
