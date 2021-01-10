package ru.everybit.bzkpd_bsk.components.manufacture.day_plan;

import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSector;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockItem;

import com.vaadin.shared.ui.grid.HeightMode;

public class TransferItemsGrid extends BpmGrid{
    private static final long serialVersionUID = -2336398060706321711L;

    public TransferItemsGrid() {
        setWidth("100%");
        setSelectionMode(SelectionMode.SINGLE);
        setHeightMode(HeightMode.ROW);
        setHeightByRows(7);
        addGridColumn("buildingObjectName", String.class, "Объект");
        addGridColumn("articleTypeName", String.class, "Тип изделия");
        addGridColumn("articleName", String.class, "Наименование изделия");
        addGridColumn("floor", String.class, "Этаж");
        addGridColumn("block", String.class, "Блок-секция");
        addGridColumn("quantity", Integer.class, "Передано на склад шт.");
        prependFooterRow();
        addFooterSum("quantity");
    }

    public void loadData(List<BpmTransferToStockItem> transferItems) {
        removeAllItems();
        for (BpmTransferToStockItem transferItem : transferItems) {
            BpmBuildingPlanItem buildingPlanItem = transferItem.getBuildingPlanItem();
            BpmBuildingPlanSector planSector = buildingPlanItem.getBuildingPlanSector();
            BpmManufactureDayPlanLineBuildingObjectItem objectItem = transferItem.getManufactureDayPlanLineBuildingObjectItem();
            BpmArticle article = objectItem.getArticle();
            addItem(transferItem);
            BpmBuildingObject buildingObject = objectItem.getBuildingObject();
            setItemProperty(transferItem, "buildingObjectName", buildingObject.getObject());
            setItemProperty(transferItem, "articleTypeName", article.getArticleType().getName());
            setItemProperty(transferItem, "articleName", article.getName());
            setItemProperty(transferItem, "floor", planSector.getFloor().getName());
            setItemProperty(transferItem, "block", planSector.getBlock().getName());
            setItemProperty(transferItem, "quantity", transferItem.getQuantity());
          }
    }
}
