package ru.everybit.bzkpd_bsk.report.building;

import com.vaadin.data.Property;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.*;

import java.util.ArrayList;
import java.util.List;

public class PickingListReport implements BpmView {
    // model
    private BpmBuildingObject buildingObject;
    // view
    private VerticalLayout layout;
    private BpmComboBox articleTypeComboBox;
    private BpmGrid productsGrid;
    private SimpleStringFilter articleTypeFilter;

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    @Override
    public void initView() {
        initLayout();
        initArticleTypeComboBox();
        initProductsGrid();
    }

    private void initLayout() {
        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);
    }

    private void initArticleTypeComboBox() {
        articleTypeComboBox = new BpmComboBox();
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.setNullSelectionAllowed(false);
        articleTypeComboBox.setTextInputAllowed(false);
        articleTypeComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
            }
        });
    }

    private void initProductsGrid() {
        productsGrid = new BpmGrid();
        productsGrid.setWidth("100%");
        productsGrid.addFrozenColumn("articleType", "Тип изделия", 140).setHidden(true);
        productsGrid.addFrozenColumn("articleName", "Наименование", 140);
        productsGrid.addFrozenColumn("articleKji", "Обозначение", 180);
        layout.addComponent(productsGrid);

        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "floor.priority"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "block.priority"));
        List<BpmSector> sectorList = BpmDaoFactory.getSectorDao().findByFloorBuildingObject(buildingObject, new Sort(orderList));

        for (BpmSector sector : sectorList) {
            productsGrid.addGridColumn(sector, Integer.class, sector.getBlock().getName(), 60);
        }
        Grid.HeaderRow prependHeaderRow = productsGrid.prependHeaderRow();
        BpmFloor lastFloor = null;
        List<BpmSector> groupByBlockList = new ArrayList<>();
        for (BpmSector sector : sectorList) {
            BpmFloor currentFloor = sector.getFloor();
            if (lastFloor != null && !currentFloor.equals(lastFloor)) {
                joinColumns(prependHeaderRow, groupByBlockList, lastFloor);
            }
            groupByBlockList.add(sector);
            lastFloor = currentFloor;
        }
        if (!groupByBlockList.isEmpty() && lastFloor != null) {
            joinColumns(prependHeaderRow, groupByBlockList, lastFloor);
        }
        prependHeaderRow.join("articleType", "articleName", "articleKji").setComponent(articleTypeComboBox);

        orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "sector.floor.priority"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "sector.block.priority"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingObjectArticle.article.articleType.name"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "buildingObjectArticle.article.name"));
        List<BpmProduct> productList = BpmDaoFactory.getProductDao().findByBuildingObjectArticleBuildingObject(buildingObject, new Sort(orderList));
        for (BpmProduct product : productList) {
            BpmArticle article = product.getBuildingObjectArticle().getArticle();
            if (productsGrid.getContainerDataSource().getItem(article) == null) {
                productsGrid.addItem(article);
                if (articleTypeComboBox.getContainerDataSource().getItem(article.getArticleType()) == null) {
                    articleTypeComboBox.addItem(article.getArticleType());
                }
                productsGrid.setItemProperty(article, "articleType", article.getArticleType().getName());
                productsGrid.setItemProperty(article, "articleName", article.getName());
                productsGrid.setItemProperty(article, "articleKji", article.getArticleKji().getName());
            }
            Integer quantity = (Integer) productsGrid.getItemProperty(article, product.getSector());
            quantity = quantity == null ? product.getQuantity() : product.getQuantity() + quantity;
            productsGrid.setItemProperty(article, product.getSector(), quantity);
        }
        articleTypeComboBox.selectFirstItem();
    }

    private void joinColumns(Grid.HeaderRow headerRow, List<BpmSector> sectorList, BpmFloor floor) {
        Grid.HeaderCell headerCell;
        if (sectorList.size() > 1) {
            headerCell = headerRow.join(sectorList.toArray());
        } else {
            headerCell = headerRow.getCell(sectorList.get(0));
            productsGrid.getColumn(sectorList.get(0)).setWidth(120);
        }
        headerCell.setText(floor.getName());
        sectorList.clear();
    }

    @Override
    public Component getView() {
        return layout;
    }

    private void eventArticleTypeSelected(BpmArticleType articleType) {
        if (articleTypeFilter != null) {
            productsGrid.removeFilter(articleTypeFilter);
        }
        productsGrid.addFilter(articleTypeFilter = new SimpleStringFilter("articleType", articleType.getName(), false, false));
    }
}
