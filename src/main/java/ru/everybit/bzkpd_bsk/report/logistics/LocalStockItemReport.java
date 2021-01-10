package ru.everybit.bzkpd_bsk.report.logistics;

import com.vaadin.data.Property;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.*;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;

import java.util.ArrayList;
import java.util.List;

public class LocalStockItemReport implements BpmView {
    // view
    private VerticalLayout layout;
    private VerticalLayout gridLayout;
    private BpmComboBox buildingObjectComboBox;
    private BpmComboBox floorComboBox;
    private BpmComboBox articleTypeComboBox;
    private SimpleStringFilter articleTypeFilter;
    private TextField stockTotalTextField;

    @Override
    public void initView() {
        initLayout();
        initStockItemsLayout();
        buildingObjectComboBox.selectFirstItem();
    }

    private void initLayout() {
        layout = new VerticalLayout();
        layout.setSpacing(true);
    }

    private void initStockItemsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setWidth("100%");
        layout.addComponent(horizontalLayout);

        Label stockLabel = new Label("Склад на объекте");
        stockLabel.setWidth("120px");
        horizontalLayout.addComponent(stockLabel);
        horizontalLayout.setComponentAlignment(stockLabel, Alignment.MIDDLE_LEFT);

        buildingObjectComboBox = new BpmComboBox();
        buildingObjectComboBox.setWidth("200px");
        buildingObjectComboBox.setNullSelectionAllowed(false);
        buildingObjectComboBox.setTextInputAllowed(false);
        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                floorComboBox.removeAllItems();
                for (BpmFloor floor : BpmDaoFactory.getFloorDao().
                        findByBuildingObjectIdOrderByPriorityAsc(((BpmBuildingObject) buildingObjectComboBox.getValue()).getId())) {
                    floorComboBox.addItem(floor);
                }
                floorComboBox.selectFirstItem();
                stockTotalTextField.setReadOnly(false);
                Float totalVolume = BpmDaoFactory.getBuildingPlanItemDao().totalVolume(((BpmBuildingObject) buildingObjectComboBox.getValue()).getId());
                stockTotalTextField.setValue(String.format("%.2f", totalVolume == null ? 0f : totalVolume));
                stockTotalTextField.setReadOnly(true);
            }
        });
        for (BpmBuildingObject buildingObject : BpmDaoFactory.getBuildingObjectDao().findAll(new Sort(Sort.Direction.ASC, "object"))) {
            buildingObjectComboBox.addItem(buildingObject);
        }
        horizontalLayout.addComponent(buildingObjectComboBox);

        floorComboBox = new BpmComboBox();
        floorComboBox.setWidth("150px");
        floorComboBox.setNullSelectionAllowed(false);
        floorComboBox.setTextInputAllowed(false);
        floorComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                initStockItemsGrid((BpmFloor) floorComboBox.getValue());
            }
        });
        horizontalLayout.addComponent(floorComboBox);

        HorizontalLayout dummyLayout = new HorizontalLayout();
        horizontalLayout.addComponent(dummyLayout);
        horizontalLayout.setExpandRatio(dummyLayout, 1);

        Label totalLabel = new Label("Всего на локальном складе, м3");
        totalLabel.setWidth("220px");
        horizontalLayout.addComponent(totalLabel);
        horizontalLayout.setComponentAlignment(totalLabel, Alignment.MIDDLE_LEFT);

        stockTotalTextField = new TextField();
        stockTotalTextField.setWidth("50px");
        horizontalLayout.addComponent(stockTotalTextField);

        gridLayout = new VerticalLayout();
        gridLayout.setWidth("100%");
        layout.addComponent(gridLayout);
    }

    private void initStockItemsGrid(BpmFloor floor) {
        gridLayout.removeAllComponents();
        if (floor == null) {
            return;
        }

        final BpmGrid grid = new BpmGrid();
        grid.setWidth("100%");
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(7);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        grid.addGridColumn("articleName", String.class, "Наименование изделия");
        gridLayout.addComponent(grid);

        Grid.HeaderRow headerRow = grid.prependHeaderRow();
        articleTypeComboBox = new BpmComboBox();
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (articleTypeFilter != null) {
                    grid.removeFilter(articleTypeFilter);
                }
                BpmArticleType articleType = (BpmArticleType) articleTypeComboBox.getValue();
                if (articleType != null) {
                    articleTypeFilter = new SimpleStringFilter("articleTypeName", articleType.getName(), true, true);
                    grid.addFilter(articleTypeFilter);
                }
            }
        });
        headerRow.getCell("articleTypeName").setComponent(articleTypeComboBox);

        for (BpmBlock block : BpmDaoFactory.getBlockDao().
                findByBuildingObjectIdOrderByPriorityAsc(((BpmBuildingObject) buildingObjectComboBox.getValue()).getId())) {
            grid.addGridColumn("quantity_" + block.getId(), Integer.class, "Доставлено").setHidden(true);
            grid.addGridColumn("planQuantity_" + block.getId(), Integer.class, "Нужно").setHidden(true);
            grid.addGridColumn("quantityReport_" + block.getId(), String.class, "Количество");
            grid.addGridColumn("volume_" + block.getId(), Float.class, "Объем, м3");
            headerRow.join("quantity_" + block.getId(), "planQuantity_" + block.getId(), "quantityReport_" + block.getId(), "volume_" + block.getId()).setText(block.getName());
        }

        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingObjectArticle.article.name"));
        for (BpmProduct product : BpmDaoFactory.getProductDao().findBySectorFloor(floor, new Sort(orderList))) {
            BpmArticle article = product.getBuildingObjectArticle().getArticle();
            Long blockId = product.getSector().getBlock().getId();
            if (grid.getContainerDataSource().getItem(article) == null) {
                grid.addItem(article);
                grid.setItemProperty(article, "articleTypeName", article.getArticleType().getName());
                grid.setItemProperty(article, "articleName", article.getName());
                if (articleTypeComboBox.getContainerDataSource().getItem(article.getArticleType()) == null) {
                    articleTypeComboBox.addItem(article.getArticleType());
                }
            }
            Integer planQuantity = (Integer) grid.getItemProperty(article, "planQuantity_" + blockId);
            planQuantity = planQuantity == null ? product.getQuantity() : planQuantity + product.getQuantity();
            grid.setItemProperty(article, "planQuantity_" + blockId, planQuantity);
            grid.setItemProperty(article, "quantityReport_" + blockId, "0 из " + planQuantity);
        }

        for (BpmBuildingPlanItem buildingPlanItem : BpmDaoFactory.getBuildingPlanItemDao().findByBuildingPlanSectorFloor(floor)) {
            BpmArticle article = buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle();
            Long blockId = buildingPlanItem.getBuildingPlanSector().getBlock().getId();
            Integer localStockQuantity = (Integer) grid.getItemProperty(article, "quantity_" + blockId);
            localStockQuantity = localStockQuantity == null ? buildingPlanItem.getLocalStockQuantity() : localStockQuantity + buildingPlanItem.getLocalStockQuantity();
            grid.setItemProperty(article, "quantity_" + blockId, localStockQuantity);
            int planQuantity = (int) grid.getItemProperty(article, "planQuantity_" + blockId);
            grid.setItemProperty(article, "quantityReport_" + blockId, localStockQuantity + " из " + planQuantity);
            grid.setItemProperty(article, "volume_" + blockId, localStockQuantity * article.getVolume());
        }
    }

    @Override
    public Component getView() {
        return layout;
    }
}
