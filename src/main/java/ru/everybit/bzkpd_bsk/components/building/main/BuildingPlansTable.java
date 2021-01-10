package ru.everybit.bzkpd_bsk.components.building.main;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import ru.everybit.bzkpd_bsk.application.view.component.BpmTable;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;

public class BuildingPlansTable extends BpmTable {
    private static final long serialVersionUID = 1L;

    public BuildingPlansTable(final BeanItemContainer<BpmBuildingPlan> buildingPlansContainer) {
        super();
        setDateFormat("dd.MM.yyyy");
        setContainerDataSource(buildingPlansContainer);

        addGeneratedColumn("status",
                new ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object generateCell(final Table source, final Object itemId, Object columnId) {
                        BeanItem<BpmBuildingPlan> item = buildingPlansContainer.getItem(itemId);
                        final BpmBuildingPlan plan = item.getBean();
                        String status = plan.getStatus();
                        if (BpmBuildingPlan.STATUS_NEW.equals(status)) {
                            return new Label("В работе");
                        }
                        if (BpmBuildingPlan.STATUS_APPROVED.equals(status)) {
                            return new Label("Согласована");
                        }
                        if (BpmBuildingPlan.STATUS_COMPLETED.equals(status)) {
                            return new Label("Завершена");
                        }
                        return new Label();
                    }
                });

        setVisibleColumns("fromDate", "toDate", "status");
        setColumnHeaders("Дата начала", "Дата завершения", "Статус");
        setSortContainerPropertyId("fromDate");
        setSortAscending(false);
        setWidth("480px");
        setHeight("240px");
    }
}
