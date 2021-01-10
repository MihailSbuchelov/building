package ru.everybit.bzkpd_bsk.processes.manufacture.month_plan.create;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.SelectionEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;

import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanForBuildingPlan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateManufacturePlanFormStep1 extends BpmWizardFormStep {
    // model
    private List<BpmBuildingPlan> buildingPlanList;
    // view
    private DateField dateField;
    private BpmGrid buildingPlanGrid;

    public CreateManufacturePlanFormStep1(BpmWizardForm wizardForm) {
        super(CreateManufacturePlanFormStep1.class.getSimpleName(), wizardForm);
        // model
        buildingPlanList = new ArrayList<>();
        // view
        dateField = new DateField();
        buildingPlanGrid = new BpmGrid();
    }

    @Override
    public void initModel() {
        for (BpmBuildingPlan buildingPlan : BpmDaoFactory.getBuildingPlanDao().
                findByStatusOrderByBuildingObjectObjectAscFromDateAsc(BpmBuildingPlan.STATUS_APPROVED)) {
            boolean lock = false;
            for (BpmManufactureMonthPlanForBuildingPlan manufactureMonthPlanForBuildingPlan : BpmDaoFactory.
                    getManufactureMonthPlanForBuildingPlanDao().findByBuildingPlan(buildingPlan)) {
                if (BpmManufactureMonthPlan.STATUS_NEW.equals(manufactureMonthPlanForBuildingPlan.getManufactureMonthPlan().getStatus())) {
                    lock = true;
                    break;
                }
            }
            if (!lock) {
                buildingPlanList.add(buildingPlan);
            }
        }
    }

    @Override
    public void initView() {
        wizardFormStepLayout.setSpacing(true);

        dateField.setCaption("Месяц");
        dateField.setResolution(Resolution.MONTH);
        wizardFormStepLayout.addComponent(dateField);

        buildingPlanGrid.setCaption("Выберите заявки на строительство");
        buildingPlanGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        buildingPlanGrid.addColumn("buildingPlan.id", Long.class).setHidden(true);
        buildingPlanGrid.addColumn("buildingPlan.buildingObject.object").setHeaderCaption("Объект").setSortable(false);
        buildingPlanGrid.addColumn("buildingPlan.buildingObject.address").setHeaderCaption("Адрес").setSortable(false);
        buildingPlanGrid.addColumn("buildingPlan.fromDate").setHeaderCaption("Дата с").setSortable(false);
        buildingPlanGrid.addColumn("buildingPlan.toDate").setHeaderCaption("Дата по").setSortable(false);
        Container.Indexed container = buildingPlanGrid.getContainerDataSource();
        for (BpmBuildingPlan buildingPlan : buildingPlanList) {
            Item item = container.addItem(buildingPlan);
            updateItem(item, buildingPlan);
        }
        buildingPlanGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                setNextButtonEnabled(!buildingPlanGrid.getSelectedRows().isEmpty());
            }
        });
        wizardFormStepLayout.addComponent(buildingPlanGrid);
        setNextButtonEnabled(false);
    }

    @SuppressWarnings("unchecked")
    private void updateItem(Item item, BpmBuildingPlan buildingPlan) {
        item.getItemProperty("buildingPlan.id").setValue(buildingPlan.getId());
        item.getItemProperty("buildingPlan.buildingObject.object").setValue(buildingPlan.getBuildingObject().getObject());
        item.getItemProperty("buildingPlan.buildingObject.address").setValue(buildingPlan.getBuildingObject().getAddress());
        item.getItemProperty("buildingPlan.fromDate").setValue(BpmUtils.formatDate(buildingPlan.getFromDate()));
        item.getItemProperty("buildingPlan.toDate").setValue(BpmUtils.formatDate(buildingPlan.getToDate()));
    }

    @Override
    public void initControl() {
        dateField.setValue(new Date());
    }

    @Override
    public void eventNextButtonClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateField.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        BpmManufactureMonthPlan manufactureMonthPlan = new BpmManufactureMonthPlan();
        manufactureMonthPlan.setMonth(calendar.getTime());
        manufactureMonthPlan.setStatus(BpmManufactureMonthPlan.STATUS_NEW);
        manufactureMonthPlan = BpmDaoFactory.getManufactureMonthPlanDao().save(manufactureMonthPlan);

        for (Object itemId : buildingPlanGrid.getSelectedRows()) {
            BpmManufactureMonthPlanForBuildingPlan manufactureBuildingPlan = new BpmManufactureMonthPlanForBuildingPlan();
            manufactureBuildingPlan.setManufactureMonthPlan(manufactureMonthPlan);
            manufactureBuildingPlan.setBuildingPlan((BpmBuildingPlan) itemId);
            BpmDaoFactory.getManufactureMonthPlanForBuildingPlanDao().save(manufactureBuildingPlan);
        }

        getForm().setProcessVariable(CreateManufacturePlanForm.VARIABLE_MANUFACTURE_MONTH_PLAN_ID, manufactureMonthPlan.getId());
        getForm().setProcessVariable(BpmProcessModel.TITLE_VARIABLE, BpmUtils.formatMonth(calendar.getTime()));
    }
}
