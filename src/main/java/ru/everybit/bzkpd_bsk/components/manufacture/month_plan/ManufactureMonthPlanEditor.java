package ru.everybit.bzkpd_bsk.components.manufacture.month_plan;

import com.vaadin.ui.*;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanItem;

import java.util.HashMap;
import java.util.Map;

public class ManufactureMonthPlanEditor extends BpmWindow {
    private Map<TextField, BpmManufactureMonthPlanItem> manufactureLineTextFieldMap;
    private ManufactureMonthPlanControl control;
    private ManufactureMonthPlanModel model;

    public ManufactureMonthPlanEditor(ManufactureMonthPlanControl control, ManufactureMonthPlanModel model) {
        super("Изменить план для изделия");
        this.control = control;
        this.model = model;
        manufactureLineTextFieldMap = new HashMap<>();
        init();
    }

    private void init() {
        BpmArticle article = model.getArticle();

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        TextField articleNameTextField = new TextField("Изделие");
        articleNameTextField.setWidth("100%");
        articleNameTextField.setValue(article.getName());
        articleNameTextField.setReadOnly(true);
        formLayout.addComponent(articleNameTextField);
        for (BpmManufactureLine manufactureLine : model.getManufactureLineList()) {
            TextField manufactureLineTextField = new TextField(manufactureLine.getName());
            manufactureLineTextField.setWidth("100%");
            BpmManufactureMonthPlanItem manufacturePlanItem = model.getManufacturePlanItem(manufactureLine, article);
            manufactureLineTextField.setValue(manufacturePlanItem.getQuantity().toString());
            formLayout.addComponent(manufactureLineTextField);
            manufactureLineTextFieldMap.put(manufactureLineTextField, manufacturePlanItem);
        }

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
        for (TextField manufactureLineTextField : manufactureLineTextFieldMap.keySet()) {
            String value = manufactureLineTextField.getValue();
            boolean delete = false;
            if ("".equals(value)) {
                delete = true;
            }
            int quantity = Integer.valueOf(value);
            if (quantity == 0) {
                delete = true;
            }
            BpmManufactureMonthPlanItem manufacturePlanItem = manufactureLineTextFieldMap.get(manufactureLineTextField);
            if (delete) {
                model.deleteManufacturePlanItem(manufacturePlanItem);
            } else {
                manufacturePlanItem.setQuantity(quantity);
                model.saveManufacturePlanItem(manufacturePlanItem);
            }
        }
        control.eventManufactureMonthPlanItemsSaved();
        close();
    }
}
