package ru.everybit.bzkpd_bsk.components.manufacture.day_plan;

import java.util.List;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;

import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;

public class ManufactureDayPlanLineBuildingObjectItemGrid extends BpmGrid {
    private static final long serialVersionUID = 1027047930441028472L;

    public ManufactureDayPlanLineBuildingObjectItemGrid() {
        setWidth("100%");
        setSelectionMode(Grid.SelectionMode.SINGLE);
        setHeightMode(HeightMode.ROW);
        setHeightByRows(7);
        addGridColumn("manufactureLine", String.class, "Линия");
        addGridColumn("buildingObjectName", String.class, "Объект");
        addGridColumn("articleTypeName", String.class, "Тип изделия");
        addGridColumn("articleName", String.class, "Наименование");
        addGridColumn("manufactureDayPlanPlanQuantity", Integer.class, "Запланировано");
        addGridColumn("manufactureDayPlanQuantity", Integer.class, "Заформовано");
        addGridColumn("manufactureDayPlanStockQuantity", Integer.class, "Передано");
        prependFooterRow();
        addFooterSum("manufactureDayPlanPlanQuantity");
        addFooterSum("manufactureDayPlanQuantity");
        addFooterSum("manufactureDayPlanStockQuantity");
    }

    public void loadData(List<BpmManufactureDayPlanLineBuildingObjectItem> objectItems) {
        removeAllItems();
        for (BpmManufactureDayPlanLineBuildingObjectItem objectItem : objectItems) {
            BpmArticle article = objectItem.getArticle();
            addItem(objectItem);
            setItemProperty(objectItem, "manufactureLine", objectItem.getManufactureDayPlanLine().getManufactureLine().getName());
            setItemProperty(objectItem, "buildingObjectName", objectItem.getBuildingObject().getObject());
            setItemProperty(objectItem, "articleTypeName", article.getArticleType().getName());
            setItemProperty(objectItem, "articleName", article.getName());
            setItemProperty(objectItem, "manufactureDayPlanPlanQuantity", objectItem.getPlanQuantity());
            setItemProperty(objectItem, "manufactureDayPlanQuantity", objectItem.getQuantity());
            setItemProperty(objectItem, "manufactureDayPlanStockQuantity", objectItem.getStockQuantity());
          }
    }
}
