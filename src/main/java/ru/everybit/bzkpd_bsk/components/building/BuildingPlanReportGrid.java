package ru.everybit.bzkpd_bsk.components.building;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReport;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReportSector;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSector;

import java.text.SimpleDateFormat;
import java.util.*;

public class BuildingPlanReportGrid extends BpmGrid {
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public BuildingPlanReportGrid(final Long buildingPlanReportId, List<BpmBuildingPlanReport> reports) {
        super();
        setWidth("100%");
        setEditorEnabled(true);
        addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                editItem(itemClickEvent.getItemId());
            }
        });
        addColumn("sector", Long.class).setHidden(true);
        addGridColumn("floor", String.class, "Этаж", 220).setEditable(false);
        addGridColumn("block", String.class, "Блок секция", 220).setEditable(false);
        addGridColumn("total", String.class, "Всего, шт.", 220).setEditable(false);

        HashMap<BpmBuildingPlanSector, List<Object>> rows = new HashMap<>();


        for (BpmBuildingPlanReport report : reports) {
            Column reportColumn = addGridColumn("report-" + report.getId(), String.class, SIMPLE_DATE_FORMAT.format(report.getReportDate()), 120);
            Set<BpmBuildingPlanReportSector> sectors = report.getSectors();
            for (BpmBuildingPlanReportSector reportSector : sectors) {
                BpmBuildingPlanSector sector = reportSector.getSector();
                List<Object> row = rows.get(sector);
                if (row == null) {
                    row = new ArrayList<>();
                    row.add(sector.getId());
                    row.add(sector.getFloor().getName());
                    row.add(sector.getBlock().getName());
                    int total = 0;
                    List<BpmBuildingPlanItem> sectorPlanItems = sector.getItems();
                    for (BpmBuildingPlanItem sectorPlanItem : sectorPlanItems) {
                        total += sectorPlanItem.getProduct().getQuantity();
                    }
                    row.add(String.valueOf(total));
                    rows.put(sector, row);
                }
                row.add(String.valueOf(reportSector.getCompletion()) + "%");
            }
            reportColumn.setEditable(report.getId().equals(buildingPlanReportId));

        }
        Collection<List<Object>> values = rows.values();
        for (List<Object> list : values) {
            addRow(list.toArray());
        }

        addRowSaveListener(new BpmGrid.BpmRowSaveListener() {

            @Override
            public void rowSave(Item item) {
                Long sectorId = (Long) item.getItemProperty("sector").getValue();
                String value = (String) item.getItemProperty("report-" + buildingPlanReportId).getValue();
                if (value.contains("%")) {
                    value = value.substring(0, value.lastIndexOf("%"));
                }
                Integer completion = Integer.valueOf(value);
                BpmBuildingPlanReportSector reportSector = BpmDaoFactory.getBuildingPlanReportSectorDao().findByReportIdAndSectorId(buildingPlanReportId, sectorId);
                reportSector.setCompletion(completion);
                BpmDaoFactory.getBuildingPlanReportSectorDao().save(reportSector);
            }
        });
    }

}
