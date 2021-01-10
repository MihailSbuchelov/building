package ru.everybit.bzkpd_bsk.components.building;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.data.domain.Sort;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.*;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSector;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BpmCreateBuildingPlanLayout extends VerticalLayout {
    private static final long serialVersionUID = -4271295374006590766L;

    class BuildingItemPlanPanel extends HorizontalLayout {
        private static final long serialVersionUID = 1L;
        private BpmBuildingPlanItem buildingPlanItem;
        private TextField quantityField;

        public BuildingItemPlanPanel(BpmBuildingPlanItem buildingPlanItem) {
            // data
            this.buildingPlanItem = buildingPlanItem;
            BpmProduct product = buildingPlanItem.getProduct();
            BpmBuildingObjectArticle objectArticle = product.getBuildingObjectArticle();
            BpmArticle article = objectArticle.getArticle();
            BpmArticleType articleType = article.getArticleType();
            // ui
            TextField articleTypeTextField = new TextField();
            articleTypeTextField.setValue(articleType.getName());
            articleTypeTextField.setReadOnly(true);
            articleTypeTextField.addStyleName("borderless");
            articleTypeTextField.addStyleName("div-grid-cell");
            articleTypeTextField.setWidth("220px");
            addComponent(articleTypeTextField);
            setComponentAlignment(articleTypeTextField, Alignment.MIDDLE_LEFT);

            TextField articleTextField = new TextField();
            articleTextField.setValue(article.getName());
            articleTextField.setReadOnly(true);
            articleTypeTextField.addStyleName("borderless");
            articleTextField.addStyleName("div-grid-cell");
            articleTextField.setWidth("220px");
            addComponent(articleTextField);
            setComponentAlignment(articleTextField, Alignment.MIDDLE_LEFT);

            quantityField = new TextField();
            quantityField.setWidth("100px");
            quantityField.addStyleName("div-grid-cell");
            quantityField.setValue(String.valueOf(buildingPlanItem.getQuantity()));
            addComponent(quantityField);
            setComponentAlignment(quantityField, Alignment.MIDDLE_LEFT);

            Label productQuantityLabel = new Label(String.valueOf(product.getQuantity()));
            productQuantityLabel.setWidth("100px");
            productQuantityLabel.addStyleName("div-grid-cell");
            addComponent(productQuantityLabel);
            setComponentAlignment(productQuantityLabel, Alignment.MIDDLE_LEFT);
        }

        public BpmBuildingPlanItem getBuildingPlanItem() {
            buildingPlanItem.setQuantity(Integer.valueOf(quantityField.getValue()));
            return buildingPlanItem;
        }
    }

    class BuildingSectorPlanPanel extends VerticalLayout {
        private static final long serialVersionUID = 1L;
        private BpmBuildingPlanSector buildingPlanSector;
        private VerticalLayout itemsLayout;

        public BuildingSectorPlanPanel(BpmBuildingPlanSector buildingPlanSector) {
            super();
            this.buildingPlanSector = buildingPlanSector;

            // Заголовок таблички
            HorizontalLayout sectorLayoutColumnHeaders = new HorizontalLayout();
            Label floorColumnLabel = new Label("Этаж");
            floorColumnLabel.addStyleName("div-grid-header");
            floorColumnLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            floorColumnLabel.setWidth("220px");
            sectorLayoutColumnHeaders.addComponent(floorColumnLabel);
            Label blockColumnLabel = new Label("Блок-секция");
            blockColumnLabel.addStyleName("div-grid-header");
            blockColumnLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            blockColumnLabel.setWidth("424px");
            sectorLayoutColumnHeaders.addComponent(blockColumnLabel);
            addComponent(sectorLayoutColumnHeaders);

            // Формируем представление для полей сектора
            HorizontalLayout sectorFieldsLayout = new HorizontalLayout();
            sectorFieldsLayout.setHeight("40px");

            Label floorLabel = new Label(buildingPlanSector.getFloor().getName());
            floorLabel.setWidth("220px");
            floorLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            floorLabel.addStyleName("div-grid-cell");

            Label blockLabel = new Label(buildingPlanSector.getBlock().getName());
            blockLabel.setWidth("424px");
            blockLabel.addStyleName(BpmThemeStyles.LABEL_BOLD);
            blockLabel.addStyleName("div-grid-cell");

            Button deleteButton = new Button("X");
            deleteButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    sectorsLayout.removeComponent(BuildingSectorPlanPanel.this);
                }
            });
            sectorFieldsLayout.addComponent(floorLabel);
            sectorFieldsLayout.addComponent(blockLabel);
            sectorFieldsLayout.addComponent(deleteButton);

            addComponent(sectorFieldsLayout);

            itemsLayout = new VerticalLayout();
            itemsLayout.setWidth("700px");

            // Заголовок таблички
            HorizontalLayout itemsLayoutColumnHeaders = new HorizontalLayout();
            Label articleTypeLabel = new Label("Тип изделя");
            articleTypeLabel.addStyleName("div-grid-header");
            articleTypeLabel.setWidth("220px");
            itemsLayoutColumnHeaders.addComponent(articleTypeLabel);
            Label articleLabel = new Label("Наименование изделия");
            articleLabel.addStyleName("div-grid-header");
            articleLabel.setWidth("220px");
            itemsLayoutColumnHeaders.addComponent(articleLabel);
            Label planQuantityLabel = new Label("Количество");
            planQuantityLabel.addStyleName("div-grid-header");
            planQuantityLabel.setWidth("100px");
            itemsLayoutColumnHeaders.addComponent(planQuantityLabel);
            Label productQuantityLabel = new Label("Всего");
            productQuantityLabel.addStyleName("div-grid-header");
            productQuantityLabel.setWidth("100px");
            itemsLayoutColumnHeaders.addComponent(productQuantityLabel);

            itemsLayout.addComponent(itemsLayoutColumnHeaders);

            // Формируем тело таблички
            List<BpmBuildingPlanItem> items = buildingPlanSector.getItems();
            for (BpmBuildingPlanItem buildingPlanItem : items) {
                BuildingItemPlanPanel itemPanel = new BuildingItemPlanPanel(buildingPlanItem);
                itemsLayout.addComponent(itemPanel);
            }
            addComponent(itemsLayout);
        }

        public BpmBuildingPlanSector getBuildingSectionPlan() {
            List<BpmBuildingPlanItem> items = new ArrayList<>();
            int componentCount = itemsLayout.getComponentCount();
            for (int i = 0; i < componentCount; i++) {
                Component component = itemsLayout.getComponent(i);
                if (component instanceof BuildingItemPlanPanel) {
                    BuildingItemPlanPanel itemPlanPanel = (BuildingItemPlanPanel) component;
                    BpmBuildingPlanItem buildingPlanItem = itemPlanPanel.getBuildingPlanItem();
                    items.add(buildingPlanItem);
                }
            }
            buildingPlanSector.setItems(items);
            return buildingPlanSector;
        }
    }

    // ui
    private BeanItemContainer<BpmBlock> blockContainer;
    private BeanItemContainer<BpmFloor> floorContainer;
    private VerticalLayout sectorsLayout;
    private BpmBuildingPlan buildingPlan;
    private DateField fromDateField;
    private DateField toDateField;

    public BpmCreateBuildingPlanLayout(BpmBuildingObject buildingObject) {
        super();
        setSpacing(true);

        // Панель "Объект строительства"
        Label buildingObjectLabel = new Label("Объект строительства");
        buildingObjectLabel.addStyleName(BpmThemeStyles.LABEL_H4);
        addComponent(buildingObjectLabel);

        BpmBuildingObjectLayout buildingObjectLayout = new BpmBuildingObjectLayout(buildingObject);
        addComponent(buildingObjectLayout);

        // Инициируем контейнеры для comboboxов
        BpmBlockDao blockDao = BpmDaoFactory.getBlockDao();
        List<BpmBlock> blockList = blockDao.findByBuildingObjectIdOrderByPriorityAsc(buildingObject.getId());
        blockContainer = new BeanItemContainer<>(BpmBlock.class, blockList);

        BpmFloorDao floorDao = BpmDaoFactory.getFloorDao();
        List<BpmFloor> floorList = floorDao.findByBuildingObjectIdOrderByPriorityAsc(buildingObject.getId());
        floorContainer = new BeanItemContainer<>(BpmFloor.class, floorList);

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
        sectorsLayout.setWidth("700px");
        sectionGridLayout.addComponent(sectorsLayout);

        // Кнопка добавить в конце таблички
        Button addSectionButton = new Button("Добавить блок-секцию");
        addSectionButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                eventAddPlanSector();
            }
        });
        sectionGridLayout.addComponent(addSectionButton);

        addComponent(sectionGridLayout);
    }

    public void loadRecord(BpmBuildingPlan buildingPlan) {
        this.buildingPlan = buildingPlan;
        fromDateField.setValue(buildingPlan.getFromDate());
        toDateField.setValue(buildingPlan.getToDate());
        Set<BpmBuildingPlanSector> sectors = buildingPlan.getSectors();
        for (BpmBuildingPlanSector buildingPlanSector : sectors) {
            BuildingSectorPlanPanel sectorPlanPanel = new BuildingSectorPlanPanel(buildingPlanSector);
            sectorsLayout.addComponent(sectorPlanPanel);
        }
    }

    public BpmBuildingPlan getRecord() {
        Set<BpmBuildingPlanSector> sectors = new LinkedHashSet<>();
        int componentCount = sectorsLayout.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            Component component = sectorsLayout.getComponent(i);
            if (component instanceof BuildingSectorPlanPanel) {
                BuildingSectorPlanPanel sectionPlanPanel = (BuildingSectorPlanPanel) component;
                BpmBuildingPlanSector buildingSectionPlan = sectionPlanPanel.getBuildingSectionPlan();
                if (buildingSectionPlan.getFloor() != null && buildingSectionPlan.getBlock() != null) {
                    sectors.add(buildingSectionPlan);
                }
            }
        }
        this.buildingPlan.setFromDate(fromDateField.getValue());
        this.buildingPlan.setToDate(toDateField.getValue());
        this.buildingPlan.setSectors(sectors);
        return this.buildingPlan;
    }

    private void eventAddPlanSector() {
        final BpmWindow window = new BpmWindow("Добавить сектор");
        FormLayout layout = new FormLayout();
        layout.setWidth("280px");
        layout.setMargin(true);


        final ComboBox floorField = new ComboBox("Этаж");
        floorField.setNullSelectionAllowed(false);
        floorField.setFilteringMode(FilteringMode.CONTAINS);
        floorField.setContainerDataSource(floorContainer);
        layout.addComponent(floorField);

        final ComboBox blockField = new ComboBox("Блок-секция");
        blockField.setNullSelectionAllowed(false);
        blockField.setContainerDataSource(blockContainer);
        layout.addComponent(blockField);

        Button button = new Button("Добавить");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                BpmFloor floor = (BpmFloor) floorField.getValue();
                BpmBlock block = (BpmBlock) blockField.getValue();
                if (floor == null || block == null) {
                    return;
                }
                // Выбрать изделия из комплектовочной ведомости для этой секции
                BpmProductDao productDao = BpmDaoFactory.getProductDao();
                List<BpmProduct> products = productDao.findByFloorAndBlock(floor.getId(), block.getId());
                // Сформировать план для списка изделий
                ArrayList<BpmBuildingPlanItem> items = new ArrayList<>();
                for (BpmProduct product : products) {
                    BpmBuildingPlanItem item = new BpmBuildingPlanItem();
                    item.setProduct(product);
                    item.setQuantity(product.getQuantity());
                    items.add(item);
                }
//                grid.addRow(getCurrentPickingListArticleModel().addNewBuildingObjectArticle(article));
                BpmBuildingPlanSector sector = new BpmBuildingPlanSector();
                sector.setFloor(floor);
                sector.setBlock(block);
                sector.setItems(items);

                BuildingSectorPlanPanel sectorPlanPanel = new BuildingSectorPlanPanel(sector);
                sectorsLayout.addComponent(sectorPlanPanel);
                window.close();
            }
        });
        layout.addComponent(button);

        window.setContent(layout);
        window.show();
    }
}
