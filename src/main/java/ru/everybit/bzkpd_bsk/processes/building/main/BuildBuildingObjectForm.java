package ru.everybit.bzkpd_bsk.processes.building.main;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.components.building.BpmBuildingObjectLayout;
import ru.everybit.bzkpd_bsk.components.building.main.BuildingPlansTable;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;

import java.util.HashMap;
import java.util.List;

public class BuildBuildingObjectForm extends BpmForm {
    private static final long serialVersionUID = -8683154940292436543L;
    private BpmBuildingObject buildingObject;
    private BeanItemContainer<BpmBuildingPlan> buildingPlansContainer;

    public BuildBuildingObjectForm() {
        super(false);
    }
    
    @Override
    public void initUiContent() {
        initBuildBuildingObjectModel();
        Label buildingObjectLabel = new Label("Объект строительства");
        buildingObjectLabel.addStyleName(BpmThemeStyles.LABEL_H4);
        formContentLayout.addComponent(buildingObjectLabel);

        BpmBuildingObjectLayout buildingObjectLayout = new BpmBuildingObjectLayout(buildingObject);
        formContentLayout.addComponent(buildingObjectLayout);

        HorizontalLayout plansLayout = new HorizontalLayout();
        plansLayout.setWidth("480px");
        Label buildingPlansLabel = new Label("Заявки на строительство");
        buildingPlansLabel.addStyleName(BpmThemeStyles.LABEL_H2);
        buildingPlansLabel.setWidth("360px");
        plansLayout.addComponent(buildingPlansLabel);

        List<BpmBuildingPlan> buildingPlans = BpmDaoFactory.getBuildingPlanDao().findByBuildingObject(buildingObject);
        buildingPlansContainer = new BeanItemContainer<>(BpmBuildingPlan.class, buildingPlans);
        BuildingPlansTable buildingPlansTable = new BuildingPlansTable(buildingPlansContainer);

        Button createBuildingMonthPlanButton = new Button(FontAwesome.PLUS_SQUARE);
        createBuildingMonthPlanButton.setDescription("Создать заявку на строительство");
        createBuildingMonthPlanButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        createBuildingMonthPlanButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                new CreateBuildingMonthPlanEditor().show();
            }
        });
        plansLayout.addComponent(createBuildingMonthPlanButton);
        plansLayout.setComponentAlignment(createBuildingMonthPlanButton, Alignment.MIDDLE_RIGHT);
        formContentLayout.addComponent(plansLayout);


        formContentLayout.addComponent(buildingPlansTable);
    }

    private Long initBuildBuildingObjectModel() {
        Long buildingObjectId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
        return buildingObjectId;
    }

    private class CreateBuildingMonthPlanEditor extends BpmWindow {
        private DateField fromDateField;
        private DateField toDateField;

        private CreateBuildingMonthPlanEditor() {
            super("Создать заявку на строительство");

            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setMargin(true);
            verticalLayout.setSpacing(true);
            setContent(verticalLayout);

            FormLayout formLayout = new FormLayout();
            formLayout.setWidth("300px");
            verticalLayout.addComponent(formLayout);

            fromDateField = new DateField("С");
            fromDateField.setWidth("100%");
            formLayout.addComponent(fromDateField);

            toDateField = new DateField("По");
            toDateField.setWidth("100%");
            formLayout.addComponent(toDateField);

            Button createButton = new Button("Создать");
            createButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            createButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    eventCreateButtonClick();
                }
            });
            verticalLayout.addComponent(createButton);
            verticalLayout.setComponentAlignment(createButton, Alignment.MIDDLE_CENTER);
        }

        private void eventCreateButtonClick() {
            BpmBuildingPlan buildingPlan = new BpmBuildingPlan();
            try {
                fromDateField.addValidator(new NullValidator("Дата начала не может быть пустой", false));
                fromDateField.validate();
                toDateField.addValidator(new NullValidator("Дата окончания не может быть пустой", false));
                toDateField.validate();
            } catch (InvalidValueException e) {
                Notification notification = new Notification("Неправильная дата начала или конца заявки.", Notification.Type.ERROR_MESSAGE);
                notification.setDelayMsec(2000);
                notification.setPosition(Position.BOTTOM_RIGHT);
                notification.show(Page.getCurrent());
                return;
            }

            buildingPlan.setFromDate(fromDateField.getValue());
            buildingPlan.setToDate(toDateField.getValue());
            buildingPlan.setBuildingObject(buildingObject);
            buildingPlan = BpmDaoFactory.getBuildingPlanDao().save(buildingPlan);
            HashMap<String, Object> variables = new HashMap<>();
            variables.put(BpmBuildingProcessModel.BUILDING_PLAN_ID, buildingPlan.getId());
            variables.put(BpmProcessModel.TYPE_VARIABLE, "План строительства");
            StringBuilder titleBuilder = new StringBuilder();
            titleBuilder.append(buildingObject.getObject());
            titleBuilder.append("<br>");
            titleBuilder.append(BpmUtils.formatDate(buildingPlan.getFromDate()));
            titleBuilder.append(" - ");
            titleBuilder.append(BpmUtils.formatDate(buildingPlan.getToDate()));
            variables.put(BpmProcessModel.TITLE_VARIABLE, titleBuilder.toString());
            startSubProcess("BuildingMonthPlanProcess", "ОПМ", variables);

            buildingPlansContainer.removeAllItems();
            buildingPlansContainer.addAll(BpmDaoFactory.getBuildingPlanDao().findByBuildingObject(buildingObject));

            close();
        }
    }
}
