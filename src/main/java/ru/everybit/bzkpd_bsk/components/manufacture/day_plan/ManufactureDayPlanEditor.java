package ru.everybit.bzkpd_bsk.components.manufacture.day_plan;

import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;

public class ManufactureDayPlanEditor extends BpmWindow {
    private ManufactureDayPlanControl control;
    private ManufactureDayPlanModel model;
    private TextField textField;

    public ManufactureDayPlanEditor(ManufactureDayPlanControl control, ManufactureDayPlanModel model) {
        super("Запланировано на день");
        this.control = control;
        this.model = model;
        init();
    }

    private void init() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("300px");
        textField = new TextField("Количество");
        textField.setWidth("100%");
        int planQuantity = model.getManufactureDayPlanItemQuantity(model.getArticle());
        textField.setValue(String.valueOf(planQuantity));
        formLayout.addComponent(textField);

        Button editButton = new Button("Изменить");
        editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventButtonClick();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(formLayout);
        layout.addComponent(editButton);
        layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private void eventButtonClick() {
        String value = textField.getValue();
        boolean delete = false;
        if ("".equals(value)) {
            delete = true;
        } else {
            int quantity = Integer.valueOf(value);
            if (quantity == 0) {
                delete = true;
            } else {
                model.updateManufactureDayPlanItemPlanQuantity(model.getArticle(), quantity);
            }
        }
        if (delete) {
            model.deleteManufactureDayPlanItem(model.getArticle());
        }
        control.eventManufactureDayPlanItemSaved();
        close();
    }
}