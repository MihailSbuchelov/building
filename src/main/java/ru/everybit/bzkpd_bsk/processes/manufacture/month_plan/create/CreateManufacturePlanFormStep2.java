package ru.everybit.bzkpd_bsk.processes.manufacture.month_plan.create;

import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.components.manufacture.month_plan.ManufactureMonthPlanControl;
import ru.everybit.bzkpd_bsk.components.manufacture.month_plan.ManufactureMonthPlanModel;
import ru.everybit.bzkpd_bsk.components.manufacture.month_plan.ManufactureMonthPlanView;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanForBuildingPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanItem;

public class CreateManufacturePlanFormStep2 extends BpmWizardFormStep {
    private ManufactureMonthPlanModel model;
    private ManufactureMonthPlanView view;
    private ManufactureMonthPlanControl control;

    public CreateManufacturePlanFormStep2(BpmWizardForm wizardForm) {
        super(CreateManufacturePlanFormStep2.class.getSimpleName(), wizardForm);
        // create
        model = new ManufactureMonthPlanModel();
        view = new ManufactureMonthPlanView();
        control = new ManufactureMonthPlanControl();
        // init references
        view.setModel(model);
        view.setControl(control);
        control.setModel(model);
        control.setView(view);
    }

    @Override
    public void initModel() {
        model.setManufactureMonthPlanId((Long) getForm().getProcessVariable(CreateManufacturePlanForm.VARIABLE_MANUFACTURE_MONTH_PLAN_ID));
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

//    @Override
//    public void eventNextButtonClick() {
//        BpmManufactureMonthPlan manufactureMonthPlan = model.getManufactureMonthPlan();
//        for (BpmManufactureMonthPlanForBuildingPlan manufactureMonthPlanForBuildingPlan : model.getManufactureMonthPlanForBuildingPlanList()) {
//            BpmBuildingPlan buildingPlan = manufactureMonthPlanForBuildingPlan.getBuildingPlan();
//            for (BpmBuildingPlanItem buildingPlanItem : BpmDaoFactory.getBuildingPlanItemDao().findByBuildingPlanSectorBuildingPlan(buildingPlan)) {
//                int quantity = 0;
//                for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : BpmDaoFactory.getManufactureMonthPlanItemDao().
//                        findByManufactureMonthPlanAndArticleAndBuildingObject(manufactureMonthPlan, buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle(), buildingPlanItem.getBuildingPlanSector().getBuildingPlan().getBuildingObject())) {
//                    quantity += manufactureMonthPlanItem.getQuantity();
//                }
//                buildingPlanItem.setProductionReserveQuantity(buildingPlanItem.getProductionReserveQuantity() + quantity);
//                BpmDaoFactory.getBuildingPlanItemDao().save(buildingPlanItem);
//            }
//        }
//        manufactureMonthPlan.setStatus(BpmManufactureMonthPlan.STATUS_IN_PROGRESS);
//        BpmDaoFactory.getManufactureMonthPlanDao().save(manufactureMonthPlan);
//    }
    
    
    @Override
    public void eventNextButtonClick() {
        BpmManufactureMonthPlan manufactureMonthPlan = model.getManufactureMonthPlan();
        for (BpmManufactureMonthPlanForBuildingPlan manufactureMonthPlanForBuildingPlan : model.getManufactureMonthPlanForBuildingPlanList()) {
            BpmBuildingPlan buildingPlan = manufactureMonthPlanForBuildingPlan.getBuildingPlan();
            BpmBuildingObject buildingObject = buildingPlan.getBuildingObject();
            List<BpmManufactureMonthPlanItem> monthPlanItems = BpmDaoFactory.getManufactureMonthPlanItemDao().findByManufactureMonthPlanAndBuildingObject(manufactureMonthPlan, buildingObject);
            List<BpmBuildingPlanItem> buildingPlanItems = BpmDaoFactory.getBuildingPlanItemDao().findByBuildingPlanSectorBuildingPlan(buildingPlan.getId());
            
            for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : monthPlanItems) {
                int quantity = manufactureMonthPlanItem.getQuantity();
                Long articleId = manufactureMonthPlanItem.getArticle().getId();
                int i = 0;
                while(i < buildingPlanItems.size() && quantity > 0){
                    BpmBuildingPlanItem buildingPlanItem = buildingPlanItems.get(i);
                    i++;
                    if(!buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getId().equals(articleId))
                        continue;
                    int avaliableToReserve = buildingPlanItem.getQuantity() - buildingPlanItem.getProductionReserveQuantity();
                    if(avaliableToReserve <= 0)
                        continue;
                    int reserve = Math.min(avaliableToReserve, quantity);
                    quantity -= reserve;
                    buildingPlanItem.setProductionReserveQuantity(buildingPlanItem.getProductionReserveQuantity() + reserve);
                }
                
            }
            
            BpmDaoFactory.getBuildingPlanItemDao().save(buildingPlanItems);
        }
        manufactureMonthPlan.setStatus(BpmManufactureMonthPlan.STATUS_IN_PROGRESS);
        BpmDaoFactory.getManufactureMonthPlanDao().save(manufactureMonthPlan);
    }
}
