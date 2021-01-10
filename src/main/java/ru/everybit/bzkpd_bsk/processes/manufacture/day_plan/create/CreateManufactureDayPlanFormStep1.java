package ru.everybit.bzkpd_bsk.processes.manufacture.day_plan.create;

import java.util.Date;

import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;
import ru.everybit.bzkpd_bsk.processes.manufacture.month_plan.create.CreateManufacturePlanForm;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;

public class CreateManufactureDayPlanFormStep1 extends BpmWizardFormStep {
    private Long manufactureMonthPlanId;
    private DateField dateField;

    public CreateManufactureDayPlanFormStep1(BpmWizardForm wizardForm) {
        super(CreateManufactureDayPlanFormStep1.class.getSimpleName(), wizardForm);
        dateField = new DateField();
    }

    @Override
    public void initModel() {
        manufactureMonthPlanId = (Long) getForm().getParentProcessVariable(CreateManufacturePlanForm.VARIABLE_MANUFACTURE_MONTH_PLAN_ID);
        dateField.setValue(new Date());
    }

    @Override
    public void initView() {
        wizardFormStepLayout.setSpacing(true);
        dateField.setCaption("День");
        dateField.setResolution(Resolution.DAY);
        wizardFormStepLayout.addComponent(dateField);
    }

    @Override
    public void eventNextButtonClick() {
        Date day = BpmUtils.prepareDay(dateField.getValue());
        
        BpmManufactureDayPlan manufactureDayPlan = new BpmManufactureDayPlan();
        manufactureDayPlan.setManufactureMonthPlan(BpmDaoFactory.getManufactureMonthPlanDao().findOne(manufactureMonthPlanId));
        manufactureDayPlan.setDay(day);
        manufactureDayPlan = BpmDaoFactory.getManufactureDayPlanDao().save(manufactureDayPlan);

        for (BpmManufactureLine manufactureLine : BpmDaoFactory.getManufactureLineDao().findAll()) {
            BpmManufactureDayPlanLine manufactureDayPlanLine = new BpmManufactureDayPlanLine();
            manufactureDayPlanLine.setManufactureDayPlan(manufactureDayPlan);
            manufactureDayPlanLine.setManufactureLine(manufactureLine);
            manufactureDayPlanLine.setStatus(BpmManufactureDayPlanLine.STATUS_NEW);
            BpmDaoFactory.getManufactureDayPlanLineDao().save(manufactureDayPlanLine);
        }
        
        getForm().setProcessVariable(BpmProcessModel.TITLE_VARIABLE, BpmUtils.formatDate(day));
        getForm().setProcessVariable(BpmManufactureProcessModel.MANUFACTURE_DAY_PLAN_ID, manufactureDayPlan.getId());
    }
}
