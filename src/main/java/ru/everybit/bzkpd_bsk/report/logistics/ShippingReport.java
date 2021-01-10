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
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShipping;

import java.util.Date;

public class ShippingReport implements BpmView {
    // view
    private VerticalLayout layout;
    private BpmGrid shippingGrid;
    private ComboBox buildingObjectComboBox;
    private SimpleStringFilter buildingObjectFilter;

    @Override
    public void initView() {
        layout = new VerticalLayout();
        layout.setSpacing(true);

        layout.addComponent(new Label("Рейсы"));

        shippingGrid = new BpmGrid();
        shippingGrid.setWidth("100%");
        shippingGrid.setHeightMode(HeightMode.ROW);
        shippingGrid.setHeightByRows(7);
        shippingGrid.addGridColumn("shippingNumber", Integer.class, "#");
        shippingGrid.addGridColumn("startDate", Date.class, "Дата/время отправки");
        shippingGrid.addGridColumn("endDate", Date.class, "Дата/время получения");
        shippingGrid.addGridColumn("status", String.class, "Статус");
        shippingGrid.addGridColumn("buildingObject", String.class, "Объект");
        shippingGrid.setSelectionMode(Grid.SelectionMode.NONE);
        layout.addComponent(shippingGrid);

        Grid.HeaderRow headerRow = shippingGrid.appendHeaderRow();
        buildingObjectComboBox = new ComboBox();
        buildingObjectComboBox.setWidth("100%");
        buildingObjectComboBox.addStyleName(BpmThemeStyles.COMBO_BOX_BORDERLESS);
        buildingObjectComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (buildingObjectFilter != null) {
                    shippingGrid.removeFilter(buildingObjectFilter);
                }
                BpmBuildingObject buildingObject = (BpmBuildingObject) buildingObjectComboBox.getValue();
                if (buildingObject != null) {
                    buildingObjectFilter = new SimpleStringFilter("buildingObject", buildingObject.getObject(), true, true);
                    shippingGrid.addFilter(buildingObjectFilter);
                }
            }
        });
        headerRow.getCell("buildingObject").setComponent(buildingObjectComboBox);

        for (BpmShipping shipping : BpmDaoFactory.getShippingDao().findAll(new Sort(Sort.Direction.ASC, "shippingNumber"))) {
            if (buildingObjectComboBox.getContainerDataSource().getItem(shipping.getBuildingObject()) == null) {
                buildingObjectComboBox.addItem(shipping.getBuildingObject());
            }
            shippingGrid.addItem(shipping);
            shippingGrid.setItemProperty(shipping, "shippingNumber", shipping.getShippingNumber());
            shippingGrid.setItemProperty(shipping, "startDate", shipping.getStartDate());
            shippingGrid.setItemProperty(shipping, "endDate", shipping.getEndDate());
            shippingGrid.setItemProperty(shipping, "status", shipping.getStatus());
            shippingGrid.setItemProperty(shipping, "buildingObject", shipping.getBuildingObject().getObject());
        }
    }

    @Override
    public Component getView() {
        return layout;
    }
}
