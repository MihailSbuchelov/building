package ru.everybit.bzkpd_bsk.processes.building.main.release;

import com.vaadin.data.Property;
import com.vaadin.ui.TextArea;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;

public class ReleaseBuildingObjectForm extends BpmForm {

    public ReleaseBuildingObjectForm() {
        super(false);
    }

    @Override
    protected void initUiContent() {
        final TextArea textArea = new TextArea();
        String comment = (String) getTaskVariable("comment");
        textArea.setValue(comment == null ? "" : comment);
        textArea.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setTaskVariable("comment", textArea.getValue());
            }
        });
        textArea.setWidth("100%");
        textArea.setRows(15);
        formContentLayout.addComponent(textArea);
    }
}
