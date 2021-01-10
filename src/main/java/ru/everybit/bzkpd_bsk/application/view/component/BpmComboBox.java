package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.ui.ComboBox;

import java.util.Collection;

public class BpmComboBox extends ComboBox {

    public BpmComboBox() {

    }

    public BpmComboBox(String caption) {
        super(caption);
    }

    public void selectFirstItem() {
        Collection<?> itemIds = getItemIds();
        if (!itemIds.isEmpty()) {
            setValue(itemIds.iterator().next());
        }
    }
}
