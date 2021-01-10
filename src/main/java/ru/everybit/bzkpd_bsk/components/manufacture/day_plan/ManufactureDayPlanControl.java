package ru.everybit.bzkpd_bsk.components.manufacture.day_plan;

import ru.everybit.bzkpd_bsk.application.view.component.BpmControl;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;

public class ManufactureDayPlanControl implements BpmControl {
    private ManufactureDayPlanModel model;
    private ManufactureDayPlanView view;

    public void setModel(ManufactureDayPlanModel model) {
        this.model = model;
    }

    public void setView(ManufactureDayPlanView view) {
        this.view = view;
    }

    @Override
    public void initControl() {
        view.selectFirstManufactureLine();
    }

    public void eventManufactureDayPlanLineSelected(BpmManufactureDayPlanLine manufactureDayPlanLine) {
        model.setManufactureDayPlanLine(manufactureDayPlanLine);
        view.updateBuildingObjectList();
    }

    public void eventBuildingObjectSelected(BpmBuildingObject buildingObject) {
        model.setBuildingObject(buildingObject);
        view.setManufactureDayPlanItemGridEditable(buildingObject != null);
        view.updateArticleTypes();
    }

    public void eventArticleTypeSelected(BpmArticleType articleType) {
        model.setArticleType(articleType);
        view.updateManufactureDayPlanItems();
    }

    public void eventEditManufactureDayPlanItems(BpmArticle article) {
        model.setArticle(article);
        new ManufactureDayPlanEditor(this, model).show();
    }

    public void eventManufactureDayPlanItemSaved() {
        view.updateManufactureDayPlanItem(model.getArticle());
    }
}
