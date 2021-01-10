package ru.everybit.bzkpd_bsk.components.building;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class BpmBuildingObjectLayout extends FormLayout {
    private static final long serialVersionUID = 1L;

    public BpmBuildingObjectLayout(BpmBuildingObject buildingObject) {
        super();
        TextField objectTextField = new TextField("Наименование объекта");
        objectTextField.setWidth("100%");
        objectTextField.setEnabled(false);
        addComponent(objectTextField);
        setComponentAlignment(objectTextField, Alignment.TOP_LEFT);

        TextField addressTextField = new TextField("Адрес объекта");
        addressTextField.setWidth("100%");
        addressTextField.setEnabled(false);
        addComponent(addressTextField);
        setComponentAlignment(addressTextField, Alignment.TOP_LEFT);

        objectTextField.setValue(buildingObject.getObject());
        addressTextField.setValue(buildingObject.getAddress());
        setHeight("100px");
    }
}
