package ru.everybit.bzkpd_bsk.report.logistics;

import com.vaadin.data.Property;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItem;

import java.util.ArrayList;
import java.util.List;

public class StockItemReport implements BpmView {
    // view
    private VerticalLayout layout;
    private BpmGrid stockItemsGrid;
    private ComboBox buildingObjectComboBox;
    private ComboBox articleTypeComboBox;
    private TextField stockTotalTextField;
    private SimpleStringFilter buildingObjectFilter;
    private SimpleStringFilter articleTypeFilter;
    private float stockTotal;

    public BpmGrid getStockItemsGrid() {
        return stockItemsGrid;
    }

    @Override
    public void initView() {
        initLayout();
        initStockItemsHeaderLayout();
        initStockItemsGrid();
        putModelToView();
    }

    private void initLayout() {
        layout = new VerticalLayout();
        layout.setSpacing(true);
    }

    private void initStockItemsHeaderLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setWidth("100%");
        layout.addComponent(horizontalLayout);

        Label stockLabel = new Label("Склад");
        stockLabel.setWidth("70px");
        horizontalLayout.addComponent(stockLabel);
        horizontalLayout.setComponentAlignment(stockLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout dummyLayout = new HorizontalLayout();
        horizontalLayout.addComponent(dummyLayout);
        horizontalLayout.setExpandRatio(dummyLayout, 1);

        Label totalLabel = new Label("Всего на складе, м3");
        totalLabel.setWidth("140px");
        horizontalLayout.addComponent(totalLabel);
        horizontalLayout.setComponentAlignment(totalLabel, Alignment.MIDDLE_LEFT);

        stockTotal = 0f;
        stockTotalTextField = new TextField();
        stockTotalTextField.setWidth("50px");
        stockTotalTextField.setReadOnly(true);
        horizontalLayout.addComponent(stockTotalTextField);
    }

    private void initStockItemsGrid() {
        stockItemsGrid = new BpmGrid();
        stockItemsGrid.setWidth("100%");
        stockItemsGrid.setHeightMode(HeightMode.ROW);
        stockItemsGrid.setHeightByRows(7);
        stockItemsGrid.addGridColumn("buildingObject", String.class, "Объект");
        stockItemsGrid.addGridColumn("floor", String.class, "Этаж");
        stockItemsGrid.addGridColumn("block", String.class, "Блок-секция");
        stockItemsGrid.addGridColumn("articleTypeName", String.class, "Тип изделия");
        stockItemsGrid.addGridColumn("articleName", String.class, "Наименование изделия");
        stockItemsGrid.addGridColumn("articleVolume", Float.class, "Объем изделия").setHidden(true);
        stockItemsGrid.addGridColumn("quantity", Integer.class, "Количество");
        stockItemsGrid.addGridColumn("volume", Float.class, "Объем, м3");
        stockItemsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        layout.addComponent(stockItemsGrid);

        Grid.HeaderRow headerRow = stockItemsGrid.appendHeaderRow();
        buildingObjectComboBox = new ComboBox();
        buildingObjectComboBox.setWidth("100%");
        buildingObjectComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (buildingObjectFilter != null) {
                    stockItemsGrid.removeFilter(buildingObjectFilter);
                }
                BpmBuildingObject buildingObject = (BpmBuildingObject) buildingObjectComboBox.getValue();
                if (buildingObject != null) {
                    buildingObjectFilter = new SimpleStringFilter("buildingObject", buildingObject.getObject(), true, true);
                    stockItemsGrid.addFilter(buildingObjectFilter);
                }
            }
        });
        headerRow.getCell("buildingObject").setComponent(buildingObjectComboBox);

        articleTypeComboBox = new ComboBox();
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (articleTypeFilter != null) {
                    stockItemsGrid.removeFilter(articleTypeFilter);
                }
                BpmArticleType articleType = (BpmArticleType) articleTypeComboBox.getValue();
                if (articleType != null) {
                    articleTypeFilter = new SimpleStringFilter("articleTypeName", articleType.getName(), true, true);
                    stockItemsGrid.addFilter(articleTypeFilter);
                }
            }
        });
        headerRow.getCell("articleTypeName").setComponent(articleTypeComboBox);
    }

    @Override
    public Component getView() {
        return layout;
    }

    private void putModelToView() {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.buildingObject.object"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.floor.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.sector.block.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingPlanItem.product.buildingObjectArticle.article.name"));
        List<BpmStockItem> stockItemList = BpmDaoFactory.getStockItemDao().findAll(new Sort(orderList));
        for (BpmStockItem stockItem : stockItemList) {
            BpmBuildingObject buildingObject = stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getBuildingObject();
            if (buildingObjectComboBox.getContainerDataSource().getItem(buildingObject) == null) {
                buildingObjectComboBox.addItem(buildingObject);
            }

            BpmArticleType articleType = stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType();
            if (articleTypeComboBox.getContainerDataSource().getItem(articleType) == null) {
                articleTypeComboBox.addItem(articleType);
            }

            stockItemsGrid.addItem(stockItem);
            stockItemsGrid.setItemProperty(stockItem, "buildingObject", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getBuildingObject().getObject());
            stockItemsGrid.setItemProperty(stockItem, "floor", stockItem.getBuildingPlanItem().getProduct().getSector().getFloor().getName());
            stockItemsGrid.setItemProperty(stockItem, "block", stockItem.getBuildingPlanItem().getProduct().getSector().getBlock().getName());
            stockItemsGrid.setItemProperty(stockItem, "articleTypeName", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getArticleType().getName());
            stockItemsGrid.setItemProperty(stockItem, "articleName", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getName());
            stockItemsGrid.setItemProperty(stockItem, "articleVolume", stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume());
            stockItemsGrid.setItemProperty(stockItem, "quantity", 0);
            updateQuantity(stockItem, stockItem.getQuantity());
        }
    }

    public void updateQuantity(BpmStockItem stockItem, Integer quantity) {
        stockTotalTextField.setReadOnly(false);
        stockTotal += quantity * stockItem.getBuildingPlanItem().getProduct().getBuildingObjectArticle().getArticle().getVolume();
        stockTotalTextField.setValue(String.format("%.2f", stockTotal));
        stockTotalTextField.setReadOnly(true);

        quantity += (Integer) stockItemsGrid.getItemProperty(stockItem, "quantity");
        Float volume = quantity * (Float) stockItemsGrid.getItemProperty(stockItem, "articleVolume");
        stockItemsGrid.setItemProperty(stockItem, "volume", volume);
        stockItemsGrid.setItemProperty(stockItem, "quantity", quantity);
    }
}
