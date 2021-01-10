package ru.everybit.bzkpd_bsk.components.building.main.picking_list;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmComboBox;
import ru.everybit.bzkpd_bsk.application.view.component.BpmEditButtons;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.building.building_object.*;

import java.util.ArrayList;
import java.util.List;

public class PickingListView implements BpmView {
    // model
    private PickingListModel model;
    // view
    private BpmComboBox articleTypeComboBox;
    private BpmGrid productGrid;
    private BpmEditButtons articleEditButtons;
    private VerticalLayout layout;
    // control
    private PickingListControl control;
    private BpmWizardFormStep form;

    public PickingListView(BpmWizardFormStep form) {
        // view
        articleTypeComboBox = new BpmComboBox();
        productGrid = new BpmGrid();
        articleEditButtons = new BpmEditButtons();
        // control
        this.form = form;
    }

    public void setPickingListModel(PickingListModel pickingListModel) {
        this.model = pickingListModel;
    }

    public void setPickingListControl(PickingListControl pickingListControl) {
        this.control = pickingListControl;
    }

    @Override
    public void initView() {
        initArticleTypeComboBox();
        initProductGrid();
        initArticleEditButtons();
        initLayout();
    }

    private void initArticleTypeComboBox() {
        articleTypeComboBox.setWidth("100%");
        articleTypeComboBox.setNullSelectionAllowed(false);
        articleTypeComboBox.setTextInputAllowed(false);
        articleTypeComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        List<BpmArticleType> articleTypeList = model.getArticleTypeList();
        articleTypeComboBox.setContainerDataSource(new BeanItemContainer<>(BpmArticleType.class, articleTypeList));
        articleTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                control.eventArticleTypeSelected((BpmArticleType) articleTypeComboBox.getValue());
            }
        });
    }

    private void initProductGrid() {
        productGrid.setWidth("100%");
        productGrid.addHiddenColumn("buildingObjectArticleId", Long.class);
        productGrid.addFrozenColumn("buildingObjectArticleName", "Наименование", 140);
        productGrid.addFrozenColumn("buildingObjectArticleKji", "Обозначение", 180);
        for (BpmSector sector : model.getSectorList()) {
            productGrid.addGridColumn(sector, String.class, sector.getBlock().getName(), 60);
        }
        Grid.HeaderRow prependHeaderRow = productGrid.prependHeaderRow();
        BpmFloor lastFloor = null;
        List<BpmSector> groupByBlockList = new ArrayList<>();
        for (BpmSector sector : model.getSectorList()) {
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
        prependHeaderRow.join("buildingObjectArticleName", "buildingObjectArticleKji").setComponent(articleTypeComboBox);
        productGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                articleEditButtons.selectItem(productGrid.getSelectedRow());
            }
        });
    }

    private void joinColumns(Grid.HeaderRow headerRow, List<BpmSector> sectorList, BpmFloor floor) {
        Grid.HeaderCell headerCell;
        if (sectorList.size() > 1) {
            headerCell = headerRow.join(sectorList.toArray());
        } else {
            headerCell = headerRow.getCell(sectorList.get(0));
            productGrid.getColumn(sectorList.get(0)).setWidth(120);
        }
        headerCell.setText(floor.getName());
        sectorList.clear();
    }

    private void initArticleEditButtons() {
        articleEditButtons.setSpacing(true);
        articleEditButtons.addAddListener(new BpmEditButtons.AddListener() {
            @Override
            public void eventAdd() {
                control.eventAddBuildingObjectArticle();
            }
        });
        articleEditButtons.addEditListener(new BpmEditButtons.EditListener() {
            @Override
            public void eventEdit(Object item) {
                control.eventEditBuildingObjectArticle((BpmBuildingObjectArticle) item);
            }
        });
        articleEditButtons.addDeleteListener(new BpmEditButtons.DeleteListener() {
            @Override
            public void eventDelete(Object item) {
                control.eventDeleteBuildingObjectArticle((BpmBuildingObjectArticle) item);
            }
        });
    }

    private void initLayout() {
        FormLayout formLayout = new FormLayout();
        TextField buildingObjectTextField = new TextField("Объект", model.getBuildingObject().getObject());
        buildingObjectTextField.setReadOnly(true);
        formLayout.addComponent(buildingObjectTextField);

        String comment = (String) form.getForm().getTaskVariable("buildingObject.comment");
        TextArea commentTextArea = new TextArea("Комментарий");
        commentTextArea.setRows(2);
        commentTextArea.setWidth("100%");
        if (comment != null) {
            commentTextArea.setValue(comment);
        }
        commentTextArea.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                form.getForm().setTaskVariable("buildingObject.comment", event.getProperty().getValue());
            }
        });

        layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.addComponent(formLayout);
        layout.addComponent(productGrid);
        layout.addComponent(articleEditButtons);
        layout.addComponent(commentTextArea);
    }

    @Override
    public Component getView() {
        return layout;
    }

    public void selectFirstArticleType() {
        articleTypeComboBox.selectFirstItem();
    }

    public void updateBuildingObjectArticle() {
        productGrid.getContainerDataSource().removeAllItems();

        BpmArticleType articleType = model.getArticleType();
        if (articleType == null) {
            return;
        }

        List<BpmBuildingObjectArticle> buildingObjectArticleList = model.getBuildingObjectArticleList();
        for (BpmBuildingObjectArticle buildingObjectArticle : buildingObjectArticleList) {
            updateBuildingObjectArticle(buildingObjectArticle);
        }
    }

    public void deleteBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        productGrid.getContainerDataSource().removeItem(buildingObjectArticle);
        articleTypeComboBox.markAsDirty();
    }

    @SuppressWarnings("unchecked")
    public void updateBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        Container.Indexed containerDataSource = productGrid.getContainerDataSource();
        Item item = containerDataSource.getItem(buildingObjectArticle);
        if (item == null) {
            item = containerDataSource.addItem(buildingObjectArticle);
            item.getItemProperty("buildingObjectArticleId").setValue(buildingObjectArticle.getId());
            BpmArticle article = buildingObjectArticle.getArticle();
            item.getItemProperty("buildingObjectArticleName").setValue(article.getName());
            item.getItemProperty("buildingObjectArticleKji").setValue(article.getArticleKji().getName());
        }
        for (BpmSector sector : model.getSectorList()) {
            item.getItemProperty(sector).setValue("");
        }
        for (BpmProduct product : model.getProductList(buildingObjectArticle)) {
            item.getItemProperty(product.getSector()).setValue(String.valueOf(product.getQuantity()));
        }
        articleTypeComboBox.markAsDirty();
    }
}