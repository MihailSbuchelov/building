package ru.everybit.bzkpd_bsk.processes.logistics.transfer_to_local_stock.transfer;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import org.springframework.data.domain.Sort;

import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShipping;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItem;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransferToLocalStockFormStep1 extends BpmWizardFormStep {
    // model
    private Date date;
    private Integer shippingNumber;
    private List<BpmBuildingObject> buildingObjectList;
    // view
    private BpmComboBox comboBox;

    public TransferToLocalStockFormStep1(BpmWizardForm wizardForm) {
        super(TransferToLocalStockFormStep1.class.getSimpleName(), wizardForm);
    }

    @Override
    public void init() {
        initTransferItemsFormStep1Model();
        initFormLayout();
    }

    private void initTransferItemsFormStep1Model() {
        date = (Date) getForm().getTaskVariable("date");
        if (date == null) {
            date = new Date();
            getForm().setTaskVariable("date", date);
        }

        shippingNumber = (Integer) getForm().getTaskVariable("shippingNumber");
        if (shippingNumber == null) {
            EntityManagerFactory entityManagerFactory = BpmDaoFactory.getEntityManagerFactory();
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Query nativeQuery = entityManager.createNativeQuery("select nextval('bpm_shipping_shipping_number_sequence')");
            shippingNumber = ((BigInteger) nativeQuery.getSingleResult()).intValue();
            entityManager.close();
            getForm().setTaskVariable("shippingNumber", shippingNumber);
        }

        buildingObjectList = new ArrayList<>();
        for (BpmStockItem stockItem : BpmDaoFactory.getStockItemDao().findAll(new Sort(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.buildingObject"))) {
            boolean found = false;
            for (BpmBuildingObject buildingObject : buildingObjectList) {
                if (buildingObject.getId().equals(stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getBuildingObject().getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                buildingObjectList.add(stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getBuildingObject());
            }
        }
    }

    private void initFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("300px");
        wizardFormStepLayout.addComponent(formLayout);

        DateField dateField = new DateField("День");
        dateField.setWidth("100%");
        dateField.setResolution(Resolution.DAY);
        dateField.setValue(date);
        dateField.setReadOnly(true);
        formLayout.addComponent(dateField);

        TextField textField = new TextField("Рейс");
        textField.setWidth("100%");
        textField.setValue(String.valueOf(shippingNumber));
        textField.setReadOnly(true);
        formLayout.addComponent(textField);

        comboBox = new BpmComboBox("Объект");
        comboBox.setWidth("100%");
        comboBox.setTextInputAllowed(false);
        comboBox.setNullSelectionAllowed(false);
        for (BpmBuildingObject buildingObject : buildingObjectList) {
            comboBox.addItem(buildingObject);
        }
        if (!buildingObjectList.isEmpty()) {
            comboBox.selectFirstItem();
        }
        formLayout.addComponent(comboBox);
    }

    @Override
    public void eventNextButtonClick() {
        BpmShipping shipping = new BpmShipping();
        BpmBuildingObject buildingObject = (BpmBuildingObject) comboBox.getValue();
        shipping.setBuildingObject(buildingObject);
        shipping.setShippingNumber(shippingNumber);
        shipping.setStartDate(date);
        shipping.setStatus(BpmShipping.STATUS_NEW);
        shipping = BpmDaoFactory.getShippingDao().save(shipping);
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(buildingObject.getObject());
        titleBuilder.append("<br>");
        titleBuilder.append(BpmUtils.formatDate(date));
        getForm().setProcessVariable(BpmProcessModel.TITLE_VARIABLE, titleBuilder.toString());
        getForm().setProcessVariable("shippingId", shipping.getId());
    }
}
