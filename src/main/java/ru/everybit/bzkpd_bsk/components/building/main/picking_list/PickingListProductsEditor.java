package ru.everybit.bzkpd_bsk.components.building.main.picking_list;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.model.building.building_object.*;

import java.util.List;

public class PickingListProductsEditor extends BpmWindow {
    private static final long serialVersionUID = 1L;
    // model
    private PickingListModel model;
    // view
    private ComboBox articleComboBox;
    private BpmGrid productsGrid;
    private Button productsButton;
    private HorizontalLayout quickEditLayout;
    // control
    private PickingListControl pickingListControl;
    private BpmBuildingObjectArticle buildingObjectArticle;

    public PickingListProductsEditor(PickingListModel pickingListModel, PickingListControl pickingListControl) {
        this.model = pickingListModel;
        this.pickingListControl = pickingListControl;
        init();
    }

    private void init() {
        initArticleComboBox();
        initProductsGrid();
        initQuickEditLayout();
        initLayout();
    }

    private void initArticleComboBox() {
        articleComboBox = new ComboBox();
        articleComboBox.setWidth("100%");
        articleComboBox.setFilteringMode(FilteringMode.CONTAINS);
        articleComboBox.setInputPrompt("Выберите изделие");
        articleComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                productsButton.setEnabled(articleComboBox.getValue() != null);
                checkCenter();
            }
        });
    }

    private void initProductsGrid() {
        productsGrid = new BpmGrid();
        productsGrid.setEditorEnabled(true);
        productsGrid.addColumn("floor", BpmFloor.class).setHeaderCaption("Этаж").setSortable(false).setEditable(false);
        for (BpmBlock block : model.getBlockList()) {
            productsGrid.addColumn(block).setHeaderCaption(block.getName()).setWidth(60d).setSortable(false);
        }
//        productsGrid.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//            private static final long serialVersionUID = 1L;
//            public void itemClick(ItemClickEvent itemClickEvent) {
//                if(!productsGrid.isEditorActive()){
//                    productsGrid.editItem(itemClickEvent.getItemId());
//                }
//            }
//        });
    }

    private void initQuickEditLayout() {
        quickEditLayout = new HorizontalLayout();
        Button quickEditButton = new Button("Обновить");
        quickEditButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(ClickEvent event) {
                Component component;
                Container.Indexed container = productsGrid.getContainerDataSource();
                for (Component aQuickEditLayout : quickEditLayout) {
                    component = aQuickEditLayout;
                    if (!(component instanceof TextField))
                        continue;
                    TextField textField = (TextField) component;
                    try {
                        textField.validate();
                        String value = textField.getValue();
                        if (value == null || value.isEmpty()) {
                            continue;
                        }
                        for (Object itemId : container.getItemIds()) {
                            BpmFloor bpmFloor = (BpmFloor) itemId;
                            Item item = container.getItem(bpmFloor);
                            BpmBlock block = (BpmBlock) textField.getData();
                            Property itemProperty = item.getItemProperty(block);
                            itemProperty.setValue(value);
                        }
                    } catch (Validator.InvalidValueException ignored) {
                    }
                }
            }
        });
        quickEditLayout.addComponent(quickEditButton);
        for (BpmBlock block : model.getBlockList()) {
            TextField textField = new TextField();
            textField.setData(block);
            textField.addValidator(new RegexpValidator("[0-9]*", "Неправильный формат данных"));
            textField.setInputPrompt(block.getName());
            textField.setWidth("60px");
            quickEditLayout.addComponent(textField);
            quickEditLayout.setComponentAlignment(textField, Alignment.BOTTOM_RIGHT);
        }

    }

    private void initLayout() {
        productsButton = new Button();
        productsButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        productsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                    eventButtonClick();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(articleComboBox);
        layout.addComponent(quickEditLayout);
        layout.setComponentAlignment(quickEditLayout, Alignment.MIDDLE_RIGHT);
        layout.addComponent(productsGrid);
        layout.addComponent(productsButton);
        layout.setComponentAlignment(productsButton, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private void checkCenter() {
        boolean center = (productsGrid.isVisible() && articleComboBox.getValue() == null) ||
                (!productsGrid.isVisible() && articleComboBox.getValue() != null);
        productsGrid.setVisible(articleComboBox.getValue() != null);
        quickEditLayout.setVisible(articleComboBox.getValue() != null);
        if (center) {
            center();
        }
    }

    @SuppressWarnings("unchecked")
    public void editProducts(BpmBuildingObjectArticle buildingObjectArticle) {
        this.buildingObjectArticle = buildingObjectArticle;

        articleComboBox.setNullSelectionAllowed(buildingObjectArticle == null);
        articleComboBox.removeAllItems();
        List<BpmArticle> articleList;
        if (buildingObjectArticle == null) {
            articleList = model.getDistinctArticleList();
        } else {
            articleList = model.getArticleList();
        }
        articleComboBox.setContainerDataSource(new BeanItemContainer<>(BpmArticle.class, articleList));
        if (buildingObjectArticle != null) {
            for (BpmArticle article : articleList) {
                if (article.getId().equals(buildingObjectArticle.getArticle().getId())) {
                    articleComboBox.setValue(article);
                    break;
                }
            }
        }

        Container.Indexed container = productsGrid.getContainerDataSource();
        container.removeAllItems();
        for (BpmSector sector : model.getSectorList()) {
            BpmFloor floor = sector.getFloor();
            Item item = container.getItem(floor);
            if (item == null) {
                item = container.addItem(floor);
                item.getItemProperty("floor").setValue(floor);
            }
            item.getItemProperty(sector.getBlock()).setValue("");
        }
        if (buildingObjectArticle != null) {
            for (BpmProduct product : model.getProductList(buildingObjectArticle)) {
                Item item = container.getItem(product.getSector().getFloor());
                item.getItemProperty(product.getSector().getBlock()).setValue(String.valueOf(product.getQuantity()));
            }
        }
        productsButton.setCaption(buildingObjectArticle == null ? "Добавить" : "Изменить");
        productsButton.setEnabled(buildingObjectArticle != null);

        setCaption(buildingObjectArticle == null ? "Добавить изделие" : "Изменить изделие");
        checkCenter();
        show();
    }

    private void eventButtonClick() {
        BpmArticle article = (BpmArticle) articleComboBox.getValue();
        if (article == null) {
            return;
        }

        if (buildingObjectArticle == null) {
            buildingObjectArticle = model.createBuildingObjectArticle(article);
        }

        for (BpmSector sector : model.getSectorList()) {
            Item item = productsGrid.getContainerDataSource().getItem(sector.getFloor());
            String value = (String) item.getItemProperty(sector.getBlock()).getValue();
            Integer quantity = "".equals(value) ? null : Integer.valueOf(value);
            model.updateProduct(buildingObjectArticle, sector, quantity);
        }
        pickingListControl.updateBuildingObjectArticle(buildingObjectArticle);
        close();
    }
}
