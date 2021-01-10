package ru.everybit.bzkpd_bsk.processes.manufacture.day_plan.create;

import java.util.HashMap;
import java.util.Map;

import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.ManufactureDayPlanControl;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.ManufactureDayPlanModel;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.ManufactureDayPlanView;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;

public class CreateManufactureDayPlanFormStep2 extends BpmWizardFormStep {
    private ManufactureDayPlanModel model;
    private ManufactureDayPlanView view;
    private ManufactureDayPlanControl control;

    public CreateManufactureDayPlanFormStep2(BpmWizardForm wizardForm) {
        super(CreateManufactureDayPlanFormStep2.class.getSimpleName(), wizardForm);
        // create
        model = new ManufactureDayPlanModel();
        view = new ManufactureDayPlanView();
        control = new ManufactureDayPlanControl();
        // init references
        view.setModel(model);
        view.setControl(control);
        control.setModel(model);
        control.setView(view);
    }

    @Override
    public void initModel() {
        model.setManufactureDayPlanId((Long) getForm().getProcessVariable(BpmManufactureProcessModel.MANUFACTURE_DAY_PLAN_ID));
        model.initModel();

    }

    @Override
    public void initView() {
        view.initView();
        wizardFormStepLayout.addComponent(view.getView());
    }

    @Override
    public void initControl() {
        control.initControl();
    }

    @Override
    public void eventNextButtonClick() {
        for (BpmManufactureDayPlanLine manufactureDayPlanLine : model.getManufactureDayPlanLineList()) {
            int count = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().countByManufactureDayPlanLine(manufactureDayPlanLine);
            if (count > 0) {
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put(BpmManufactureProcessModel.MANUFACTURE_DAY_PLAN_LINE_ID, manufactureDayPlanLine.getId());
                variables.put(BpmProcessModel.TYPE_VARIABLE, "План производства");
                StringBuilder titleBuilder = new StringBuilder();
                titleBuilder.append(manufactureDayPlanLine.getManufactureLine().getName());
                titleBuilder.append("<br>");
                titleBuilder.append(BpmUtils.formatDate(manufactureDayPlanLine.getManufactureDayPlan().getDay()));
                variables.put(BpmProcessModel.TITLE_VARIABLE, titleBuilder.toString());
                getForm().startSubProcess("ManufactureDayPlanLineProcess", "ПДЛ", variables);
            } else {
                BpmDaoFactory.getManufactureDayPlanLineDao().delete(manufactureDayPlanLine);
            }
        }
    }
}
