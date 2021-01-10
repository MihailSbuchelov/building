package ru.everybit.bzkpd_bsk.report.manufacture;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanForBuildingPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanItem;

import java.util.ArrayList;
import java.util.List;

public class ManufactureMonthPlanReport implements BpmView {
    // model
    private BpmManufactureMonthPlan manufactureMonthPlan;
    private List<BpmBuildingPlanItem> buildingPlanItemList;
    // view
    private VerticalLayout layout;
    private BpmComboBox articleTypeComboBox;
    private BpmGrid grid;

    public void setManufactureMonthPlan(BpmManufactureMonthPlan manufactureMonthPlan) {
        this.manufactureMonthPlan = manufactureMonthPlan;

        buildingPlanItemList = new ArrayList<>();
        List<BpmManufactureMonthPlanForBuildingPlan> manufactureMonthPlanForBuildingPlanList = BpmDaoFactory.
                getManufactureMonthPlanForBuildingPlanDao().findByManufactureMonthPlan(manufactureMonthPlan);
        for (BpmManufactureMonthPlanForBuildingPlan manufactureMonthPlanForBuildingPlan : manufactureMonthPlanForBuildingPlanList) {
            buildingPlanItemList.addAll(BpmDaoFactory.getBuildingPlanItemDao().
                    findByBuildingPlanSectorBuildingPlan(manufactureMonthPlanForBuildingPlan.getBuildingPlan().getId()));
        }
    }

    @Override
    public void initView() {
        initLayout();
        initArticleTypeComboBox();
        initManufactureMonthPlanGrid();
    }

    private void initLayout() {
        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);
    }

    private void initArticleTypeComboBox() {
        articleTypeComboBox = new BpmComboBox();
        articleTypeComboBox.setWidth("350px");
        articleTypeComboBox.setNullSelectionAllowed(false);
        articleTypeComboBox.setTextInputAllowed(false);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
            }
        });
        layout.addComponent(articleTypeComboBox);

        List<BpmManufactureMonthPlanItem> manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                findByManufactureMonthPlanOrderByArticleArticleTypeIdAsc(manufactureMonthPlan);
        Container container = articleTypeComboBox.getContainerDataSource();
        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : manufactureMonthPlanItemList) {
            if (container.getItem(manufactureMonthPlanItem.getArticle().getArticleType()) == null) {
                container.addItem(manufactureMonthPlanItem.getArticle().getArticleType());
            }
        }
    }

    private void initManufactureMonthPlanGrid() {
        grid = new BpmGrid();
        grid.setWidth("100%");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addGridColumn("articleName", String.class, "Наименование изделия");
        for (BpmManufactureLine manufactureLine : BpmDaoFactory.getManufactureLineDao().findAll()) {
            grid.addGridColumn("manufactureMonthPlanLineQuantity_" + manufactureLine.getId(), Integer.class, manufactureLine.getName() + " произведено").setHidden(true);
            grid.addGridColumn("manufactureMonthPlanLinePlanQuantity_" + manufactureLine.getId(), Integer.class, manufactureLine.getName() + " план").setHidden(true);
            grid.addGridColumn("manufactureMonthPlanLine_" + manufactureLine.getId(), String.class, manufactureLine.getName());
        }
        grid.addGridColumn("manufactureMonthPlanItemPlanQuantity", Integer.class, "Заявка на месяц план").setHidden(true);
        grid.addGridColumn("manufactureMonthPlanItemQuantity", Integer.class, "Заявка на месяц произведено").setHidden(true);
        grid.addGridColumn("manufactureMonthPlanItem", String.class, "Заявка на месяц");
        grid.addGridColumn("stockQuantity", Integer.class, "Отгружено на склад");

        layout.addComponent(grid);
    }

    @Override
    public Component getView() {
        return layout;
    }

    private void eventArticleTypeSelected(BpmArticleType articleType) {
        grid.removeAllItems();
        Container.Indexed container = grid.getContainerDataSource();

        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : BpmDaoFactory.getManufactureMonthPlanItemDao().
                findByManufactureMonthPlanAndArticleArticleTypeOrderByArticleIdAsc(manufactureMonthPlan, articleType)) {
            BpmArticle article = manufactureMonthPlanItem.getArticle();
            if (container.getItem(article) == null) {
                container.addItem(article);
                grid.setItemProperty(article, "articleName", article.getName());
            }

            Integer quantity = manufactureMonthPlanItem.getQuantity();
            updateItem(article, "manufactureMonthPlanItemPlanQuantity", quantity);
            updateItem(article, "manufactureMonthPlanLinePlanQuantity_" + manufactureMonthPlanItem.getManufactureLine().getId(), quantity);
        }

        for (BpmManufactureDayPlanLineBuildingObjectItem buildingObjectItem : BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndArticleArticleTypeOrderByArticleNameAsc(manufactureMonthPlan, articleType)) {
            BpmArticle article = buildingObjectItem.getArticle();
            Integer quantity = buildingObjectItem.getQuantity();
            updateItem(article, "manufactureMonthPlanItemQuantity", quantity);
            updateItem(article, "manufactureMonthPlanLineQuantity_" + buildingObjectItem.getManufactureDayPlanLine().getManufactureLine().getId(), quantity);
        }

        for (Object itemId : grid.getContainerDataSource().getItemIds()) {
            BpmArticle article = (BpmArticle) itemId;
            for (BpmBuildingPlanItem buildingPlanItem : buildingPlanItemList) {
                if (article.getId().equals(buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getId())) {
                    updateItem(article, "stockQuantity", buildingPlanItem.getProductionQuantity());
                }
            }
        }

        for (Object itemId : grid.getContainerDataSource().getItemIds()) {
            BpmArticle article = (BpmArticle) itemId;

            for (BpmManufactureLine manufactureLine : BpmDaoFactory.getManufactureLineDao().findAll()) {
                Integer lineQuantity = (Integer) grid.getItemProperty(article, "manufactureMonthPlanLineQuantity_" + manufactureLine.getId());
                Integer linePlanQuantity = (Integer) grid.getItemProperty(article, "manufactureMonthPlanLinePlanQuantity_" + manufactureLine.getId());
                String line = (lineQuantity == null ? 0 : lineQuantity) + " из " + (linePlanQuantity == null ? 0 : linePlanQuantity);
                grid.setItemProperty(article, "manufactureMonthPlanLine_" + manufactureLine.getId(), line);
            }

            Integer lineQuantity = (Integer) grid.getItemProperty(article, "manufactureMonthPlanItemQuantity");
            Integer linePlanQuantity = (Integer) grid.getItemProperty(article, "manufactureMonthPlanItemPlanQuantity");
            String line = (lineQuantity == null ? 0 : lineQuantity) + " из " + (linePlanQuantity == null ? 0 : linePlanQuantity);
            grid.setItemProperty(article, "manufactureMonthPlanItem", line);
        }
    }

    private void updateItem(BpmArticle article, String propertyName, Integer quantity) {
        Integer itemValue = (Integer) grid.getItemProperty(article, propertyName);
        itemValue = quantity + (itemValue == null ? 0 : itemValue);
        grid.setItemProperty(article, propertyName, itemValue);
    }

    public void selectFirstArticleType() {
        articleTypeComboBox.selectFirstItem();
    }
}
