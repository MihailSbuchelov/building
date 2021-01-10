package ru.everybit.bzkpd_bsk.components.building;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmTable;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSector;

import java.util.List;
import java.util.Set;

public class BpmBuildingPlanPanel extends VerticalLayout {
    private static final long serialVersionUID = -4271295374006590766L;

    class BuildingSectorPlanPanel extends VerticalLayout {
        private static final long serialVersionUID = 1L;

        public BuildingSectorPlanPanel(BpmBuildingPlanSector buildingPlanSector) {
            super();

            List<BpmBuildingPlanItem> items = buildingPlanSector.getItems();

            setWidth("100%");
            // Заголовок таблички
            HorizontalLayout sectorLayoutColumnHeaders = new HorizontalLayout();
//            sectorLayoutColumnHeaders.setWidth("100%");

            Label floorColumnLabel = new Label("Этаж");
            floorColumnLabel.addStyleName("div-grid-header");
            floorColumnLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            floorColumnLabel.setWidth("220px");
            sectorLayoutColumnHeaders.addComponent(floorColumnLabel);

            Label blockColumnLabel = new Label("Блок-секция");
            blockColumnLabel.addStyleName("div-grid-header");
            blockColumnLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            blockColumnLabel.setWidth("220px");
            sectorLayoutColumnHeaders.addComponent(blockColumnLabel);

            Label completionColumnLabel = new Label("Смонтировано, %");
            completionColumnLabel.addStyleName("div-grid-header");
            completionColumnLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            completionColumnLabel.setWidth("220px");
            sectorLayoutColumnHeaders.addComponent(completionColumnLabel);
            sectorLayoutColumnHeaders.setComponentAlignment(completionColumnLabel, Alignment.TOP_RIGHT);

            Label totalColumnLabel = new Label("Всего, шт.");
            totalColumnLabel.addStyleName("div-grid-header");
            totalColumnLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            totalColumnLabel.setWidth("220px");
            sectorLayoutColumnHeaders.addComponent(totalColumnLabel);
            sectorLayoutColumnHeaders.setComponentAlignment(totalColumnLabel, Alignment.TOP_RIGHT);

            addComponent(sectorLayoutColumnHeaders);

            // Формируем представление для полей сектора
            HorizontalLayout sectorFieldsLayout = new HorizontalLayout();
            sectorFieldsLayout.setHeight("40px");
//            sectorFieldsLayout.setWidth("100%");

            Label floorLabel = new Label(buildingPlanSector.getFloor().getName());
            floorLabel.setWidth("220px");
            floorLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            floorLabel.addStyleName("div-grid-cell");
            sectorFieldsLayout.addComponent(floorLabel);

            Label blockLabel = new Label(buildingPlanSector.getBlock().getName());
            blockLabel.setWidth("220px");
            blockLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            blockLabel.addStyleName("div-grid-cell");
            sectorFieldsLayout.addComponent(blockLabel);

            Label completionLabel = new Label(String.valueOf(buildingPlanSector.getCompletion()) + "%");
            completionLabel.setWidth("220px");
            completionLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            completionLabel.addStyleName("div-grid-cell");
            sectorFieldsLayout.addComponent(completionLabel);
            sectorFieldsLayout.setComponentAlignment(completionLabel, Alignment.TOP_RIGHT);

            int totalQuantity = 0;
            for (BpmBuildingPlanItem item : items) {
                totalQuantity += item.getProduct().getQuantity();
            }
            Label totalLabel = new Label(String.valueOf(totalQuantity));
            totalLabel.setWidth("220px");
            totalLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            totalLabel.addStyleName("div-grid-cell");
            sectorFieldsLayout.addComponent(totalLabel);
            sectorFieldsLayout.setComponentAlignment(totalLabel, Alignment.TOP_RIGHT);

            addComponent(sectorFieldsLayout);

            final BeanItemContainer<BpmBuildingPlanItem> buildingPlanItemsContainer = new BeanItemContainer<>(BpmBuildingPlanItem.class, items);

            BpmTable buildingPlanItemsTable = new BpmTable();
            buildingPlanItemsTable.setWidth("100%");
            buildingPlanItemsTable.setPageLength(items.size());
            buildingPlanItemsTable.setContainerDataSource(buildingPlanItemsContainer);
            buildingPlanItemsTable.addGeneratedColumn("type", new Table.ColumnGenerator() {
                private static final long serialVersionUID = 1L;

                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    BeanItem<BpmBuildingPlanItem> item = buildingPlanItemsContainer.getItem(itemId);
                    return item.getBean().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName();
                }
            });
            buildingPlanItemsTable.addGeneratedColumn("name", new Table.ColumnGenerator() {
                private static final long serialVersionUID = 1L;

                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    BeanItem<BpmBuildingPlanItem> item = buildingPlanItemsContainer.getItem(itemId);
                    return item.getBean().getProduct().getBuildingObjectArticle().getArticle().getName();
                }
            });

            buildingPlanItemsTable.addGeneratedColumn("total", new Table.ColumnGenerator() {
                private static final long serialVersionUID = 1L;

                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    BeanItem<BpmBuildingPlanItem> item = buildingPlanItemsContainer.getItem(itemId);
                    return item.getBean().getProduct().getQuantity();
                }
            });

            buildingPlanItemsTable.setVisibleColumns("type", "name", "quantity", "productionQuantity", "stockQuantity", "localStockQuantity", "total");
            buildingPlanItemsTable.setColumnHeaders("Тип изделя", "Наименование", "План, шт.", "Произведено, шт.", "На складе, шт.", "На лок. складе, шт.", "Всего, шт.");
            addComponent(buildingPlanItemsTable);
        }
    }

    // ui
    private VerticalLayout sectorsLayout;
    private DateField fromDateField;
    private DateField toDateField;

    public BpmBuildingPlanPanel(BpmBuildingObject buildingObject, BpmBuildingPlan buildingPlan) {
        super();
        setSpacing(true);

        // Панель "Объект строительства"
        Label buildingObjectLabel = new Label("Объект строительства");
        buildingObjectLabel.addStyleName(BpmThemeStyles.LABEL_H4);
        addComponent(buildingObjectLabel);

        BpmBuildingObjectLayout buildingObjectLayout = new BpmBuildingObjectLayout(buildingObject);
        addComponent(buildingObjectLayout);

        // Панель "Заявка на строительство"
        Label orderLabel = new Label("Заявка на строительство");
        orderLabel.addStyleName(BpmThemeStyles.LABEL_H4);
        addComponent(orderLabel);

        // Даты заявки
        HorizontalLayout datesLayout = new HorizontalLayout();
        datesLayout.setSpacing(true);
        Label fromLabel = new Label("c");
        datesLayout.addComponent(fromLabel);
        datesLayout.setComponentAlignment(fromLabel, Alignment.MIDDLE_CENTER);
        fromDateField = new DateField();
        datesLayout.addComponent(fromDateField);
        Label toLabel = new Label("по");
        datesLayout.addComponent(toLabel);
        datesLayout.setComponentAlignment(toLabel, Alignment.MIDDLE_CENTER);
        toDateField = new DateField();
        datesLayout.addComponent(toDateField);
        addComponent(datesLayout);

        // Блок для панели блок-секций
        VerticalLayout sectionGridLayout = new VerticalLayout();

        // Тело таблички 
        // *заполняеия в loadRecord()
        sectorsLayout = new VerticalLayout();
        sectorsLayout.setWidth("100%");
        sectionGridLayout.addComponent(sectorsLayout);
        addComponent(sectionGridLayout);

        loadRecord(buildingPlan);
    }

    public void loadRecord(BpmBuildingPlan buildingPlan) {
        fromDateField.setValue(buildingPlan.getFromDate());
        fromDateField.setReadOnly(true);
        toDateField.setValue(buildingPlan.getToDate());
        toDateField.setReadOnly(true);
        Set<BpmBuildingPlanSector> sectors = buildingPlan.getSectors();
        for (BpmBuildingPlanSector buildingPlanSector : sectors) {
            BuildingSectorPlanPanel sectorPlanPanel = new BuildingSectorPlanPanel(buildingPlanSector);
            sectorsLayout.addComponent(sectorPlanPanel);
        }
    }
}
