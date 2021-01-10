package ru.everybit.bzkpd_bsk.processes.building.month_plan;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;

import ru.everybit.bzkpd_bsk.application.service.case_management.BpmCaseManagementService;
import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReport;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReportSector;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSector;
import ru.everybit.bzkpd_bsk.components.building.BpmBuildingPlanPanel;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class ControlBuildingMonthPlanForm extends BpmForm {
    private static final long serialVersionUID = 1L;
    private BpmBuildingPlan buildingPlan;

    public ControlBuildingMonthPlanForm() {
        super(false);
    }
    
    @Override
    public void initUiContent() {
        // Загружаем информацию об Объекте строительства
        Long buildingObjectId = (Long) getParentProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        Long buildingPlanId = (Long) getProcessVariable(BpmBuildingProcessModel.BUILDING_PLAN_ID);
        buildingPlan = BpmDaoFactory.getBuildingPlanDao().findOne(buildingPlanId);

        final BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);
        BpmBuildingPlanPanel buildingPlanLayout = new BpmBuildingPlanPanel(buildingObject, buildingPlan);
        formContentLayout.addComponent(buildingPlanLayout);

        // Кнопка отчета прораба
        Button createPlanForMonthButton = new Button("Запросить отчет у прораба");
        createPlanForMonthButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        createPlanForMonthButton.addStyleName(BpmThemeStyles.BUTTON_SMALL);
        createPlanForMonthButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                if (BpmCaseManagementService.checkForSubCases(task)) {
                    Notification notification = new Notification("Отчет у прораба уже запрошен", Notification.Type.ERROR_MESSAGE);
                    notification.setDelayMsec(2000);
                    notification.setPosition(Position.BOTTOM_RIGHT);
                    notification.show(Page.getCurrent());
                    return;
                }

                final Set<BpmBuildingPlanSector> sectors = buildingPlan.getSectors();
                final BpmWindow window = new BpmWindow("Запросить отчет у прораба");
                window.setWidth("300px");
                FormLayout windowLayout = new FormLayout();
                windowLayout.setMargin(true);
                windowLayout.setSpacing(true);
                final DateField reportDateField = new DateField();
                reportDateField.setCaption("Отчет на дату");
                reportDateField.setImmediate(true);
                reportDateField.addValidator(new NullValidator("Дата отчета прораба не может быть пустой", false));
                windowLayout.addComponent(reportDateField);
                Button okButton = new Button("Запросить");
                okButton.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        try {
                            reportDateField.validate();
                        } catch (InvalidValueException e) {
                            Notification notification = new Notification("Дата отчета прораба не может быть пустой.", Notification.Type.ERROR_MESSAGE);
                            notification.setDelayMsec(2000);
                            notification.setPosition(Position.BOTTOM_RIGHT);
                            notification.show(Page.getCurrent());
                            return;
                        }

                        Date reportDate = reportDateField.getValue();
                        BpmBuildingPlanReport report = new BpmBuildingPlanReport();
                        report.setBuildingPlan(buildingPlan);
                        report.setReportDate(reportDate);
                        for (BpmBuildingPlanSector sector : sectors) {
                            BpmBuildingPlanReportSector reportSector = new BpmBuildingPlanReportSector();
                            reportSector.setSector(sector);
                            reportSector.setCompletion(0);
                            report.getSectors().add(reportSector);
                        }
                        report = BpmDaoFactory.getBuildingPlanReportDao().save(report);
                        HashMap<String, Object> variables = new HashMap<>();
                        variables.put(BpmBuildingProcessModel.BUILDING_PLAN_REPORT_ID, report.getId());
                        variables.put(BpmProcessModel.TYPE_VARIABLE, "План строительства");
                        StringBuilder titleBuilder = new StringBuilder();
                        titleBuilder.append(buildingObject.getObject());
                        titleBuilder.append("<br>");
                        titleBuilder.append(BpmUtils.formatDate(reportDate));
                        variables.put(BpmProcessModel.TITLE_VARIABLE, titleBuilder.toString());
                        
                        startSubProcess("BuildingDayPlanProcess", "ОПД", variables);
                        window.close();
                    }
                });
                windowLayout.addComponent(okButton);
                window.setContent(windowLayout);
                window.show();

            }
        });
        formContentLayout.addComponent(createPlanForMonthButton);
    }

    @Override
    protected void submitButtonClick() {
        buildingPlan.setStatus(BpmBuildingPlan.STATUS_COMPLETED);
        BpmDaoFactory.getBuildingPlanDao().save(buildingPlan);
        super.submitButtonClick();
    }

}
