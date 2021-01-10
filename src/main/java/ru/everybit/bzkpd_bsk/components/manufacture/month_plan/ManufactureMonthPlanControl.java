package ru.everybit.bzkpd_bsk.components.manufacture.month_plan;

import ru.everybit.bzkpd_bsk.application.view.component.BpmControl;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

public class ManufactureMonthPlanControl implements BpmControl {
    // model
    private ManufactureMonthPlanModel model;
    // view
    private ManufactureMonthPlanView view;

    public void setModel(ManufactureMonthPlanModel model) {
        this.model = model;
    }

    public void setView(ManufactureMonthPlanView view) {
        this.view = view;
    }

    @Override
    public void initControl() {
        eventBuildingObjectSelected(null);
    }

    public void eventBuildingObjectSelected(BpmBuildingObject buildingObject) {
        model.setBuildingObject(buildingObject);
        view.setManufactureMonthPlanItemGridEditable(buildingObject != null);
        view.updateArticleTypes();
    }

    public void eventArticleTypeSelected(BpmArticleType articleType) {
        model.setArticleType(articleType);
        view.updateManufactureMonthPlanItems();
    }

    public void eventEditManufacturePlanItems(BpmArticle article) {
        model.setArticle(article);
        new ManufactureMonthPlanEditor(this, model).show();
    }

    public void eventManufactureMonthPlanItemsSaved() {
        view.updateManufactureMonthPlanItemsForArticle(model.getArticle());
    }
}
