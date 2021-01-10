package ru.everybit.bzkpd_bsk.processes.manufacture.day_plan_line.execution;

import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.ManufactureDayPlanLineBuildingObjectItemGrid;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;

import com.vaadin.data.Property;
import com.vaadin.event.SelectionEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ManufactureDayPlanLineExecutionForm extends BpmForm {
    private static final long serialVersionUID = 1L;
    private BpmManufactureDayPlanLine manufactureDayPlanLine;
    private ManufactureDayPlanLineBuildingObjectItemGrid grid;

    public ManufactureDayPlanLineExecutionForm() {
        super(false);
    }

    @Override
    public void initUiContent() {
        initManufactureDayPlanLineModel();
        initHeaderLayout();
        initCommentsLayout();
        initGrid();
    }

    protected void initManufactureDayPlanLineModel() {
        manufactureDayPlanLine = BpmDaoFactory.getManufactureDayPlanLineDao().
                findOne((Long) getProcessVariable(BpmManufactureProcessModel.MANUFACTURE_DAY_PLAN_LINE_ID));
        if (BpmManufactureDayPlanLine.STATUS_NEW.equals(manufactureDayPlanLine.getStatus())) {
            manufactureDayPlanLine.setStatus(BpmManufactureDayPlanLine.STATUS_IN_PROGRESS);
            BpmDaoFactory.getManufactureDayPlanLineDao().save(manufactureDayPlanLine);
        }
    }

    private void initHeaderLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        formContentLayout.addComponent(horizontalLayout);

        Label label = new Label("День");
        label.setWidth("45px");
        horizontalLayout.addComponent(label);
        horizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

        DateField dateField = new DateField();
        dateField.setResolution(Resolution.DAY);
        dateField.setValue(manufactureDayPlanLine.getManufactureDayPlan().getDay());
        dateField.setReadOnly(true);
        horizontalLayout.addComponent(dateField);
        horizontalLayout.setComponentAlignment(dateField, Alignment.MIDDLE_CENTER);

        HorizontalLayout dummyLayout = new HorizontalLayout();
        horizontalLayout.addComponent(dummyLayout);
        horizontalLayout.setExpandRatio(dummyLayout, 1f);

        TextField textField = new TextField();
        textField.setValue(manufactureDayPlanLine.getManufactureLine().getName());
        textField.setReadOnly(true);
        horizontalLayout.addComponent(textField);
        horizontalLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
    }

    private void initCommentsLayout() {
        formContentLayout.addComponent(new Label("Комментарий сотрудника БЗКПД"));
        TextArea managerCommentTextArea = new TextArea();
        managerCommentTextArea.setWidth("100%");
        managerCommentTextArea.setRows(2);
        String managerComment = manufactureDayPlanLine.getManagerComment();
        managerCommentTextArea.setValue(managerComment == null ? "" : managerComment);
        managerCommentTextArea.setReadOnly(true);
        formContentLayout.addComponent(managerCommentTextArea);

        formContentLayout.addComponent(new Label("Комментарий мастера"));
        TextArea employeeCommentTextArea = new TextArea();
        employeeCommentTextArea.setWidth("100%");
        employeeCommentTextArea.setRows(2);
        String employeeComment = manufactureDayPlanLine.getEmployeeComment();
        employeeCommentTextArea.setValue(employeeComment == null ? "" : employeeComment);
        employeeCommentTextArea.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                manufactureDayPlanLine.setEmployeeComment((String) event.getProperty().getValue());
                BpmDaoFactory.getManufactureDayPlanLineDao().save(manufactureDayPlanLine);
            }
        });
        formContentLayout.addComponent(employeeCommentTextArea);
    }

    private void initGrid() {
        List<BpmManufactureDayPlanLineBuildingObjectItem> objectItems = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().findByManufactureDayPlanLineOrderByArticleIdAsc(manufactureDayPlanLine);
        grid = new ManufactureDayPlanLineBuildingObjectItemGrid();
        grid.loadData(objectItems);
        formContentLayout.addComponent(grid);
        
        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void select(SelectionEvent event) {
              BpmManufactureDayPlanLineBuildingObjectItem objectItem = (BpmManufactureDayPlanLineBuildingObjectItem) grid.getSelectedRow();
              if (objectItem == null) {
                  return;
              }
              Integer quantity = (Integer) grid.getItemProperty(objectItem, "manufactureDayPlanQuantity");
              ManufactureDayPlanQuantityEditor editor = new ManufactureDayPlanQuantityEditor(objectItem, quantity == null ? 0 : quantity);
              editor.show();
            }
        });
    }

    @Override
    protected void submitButtonClick() {
        manufactureDayPlanLine.setStatus(BpmManufactureDayPlanLine.STATUS_DONE);
        BpmDaoFactory.getManufactureDayPlanLineDao().save(manufactureDayPlanLine);
        super.submitButtonClick();
    }

    private class ManufactureDayPlanQuantityEditor extends BpmWindow {
        private static final long serialVersionUID = 1L;

        ManufactureDayPlanQuantityEditor(final BpmManufactureDayPlanLineBuildingObjectItem objectItem, Integer quantity) {
            super("Произведено шт.");
            BpmArticle article = objectItem.getArticle();
            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            TextField articleTypeNameNameTextField = new TextField("Тип изделия");
            articleTypeNameNameTextField.setWidth("100%");
            articleTypeNameNameTextField.setValue(article.getArticleType().getName());
            articleTypeNameNameTextField.setReadOnly(true);
            layout.addComponent(articleTypeNameNameTextField);

            TextField articleNameTextField = new TextField("Наименование изделия");
            articleNameTextField.setWidth("100%");
            articleNameTextField.setValue(article.getName());
            articleNameTextField.setReadOnly(true);
            layout.addComponent(articleNameTextField);

            TextField manufactureDayPlanPlanQuantityTextField = new TextField("Запланировано на день шт.");
            manufactureDayPlanPlanQuantityTextField.setWidth("100%");
            manufactureDayPlanPlanQuantityTextField.setValue(String.valueOf(objectItem.getPlanQuantity()));
            manufactureDayPlanPlanQuantityTextField.setReadOnly(true);
            layout.addComponent(manufactureDayPlanPlanQuantityTextField);

            final ComboBox manufactureDayPlanQuantityComboBox = new ComboBox("Заформовано шт.");
            manufactureDayPlanQuantityComboBox.setWidth("100%");
            manufactureDayPlanQuantityComboBox.setNullSelectionAllowed(false);
            manufactureDayPlanQuantityComboBox.setTextInputAllowed(false);
            for (int i = 0; i <= objectItem.getPlanQuantity(); i++) {
                manufactureDayPlanQuantityComboBox.addItem(i);
            }
            manufactureDayPlanQuantityComboBox.setValue(quantity);
            layout.addComponent(manufactureDayPlanQuantityComboBox);
            
            Button editButton = new Button("Изменить");
            editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    int quantity = (Integer) manufactureDayPlanQuantityComboBox.getValue();
                    objectItem.setQuantity(quantity);
                    grid.setItemProperty(objectItem, "manufactureDayPlanQuantity", quantity);
                    BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().save(objectItem);
                    grid.deselect(grid.getSelectedRow());
                    close();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }
    }
}
