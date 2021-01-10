package ru.everybit.bzkpd_bsk.processes.manufacture.day_plan.create;

import com.vaadin.event.SelectionEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;

import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.components.manufacture.day_plan.ManufactureDayPlanLineBuildingObjectItemGrid;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;

import java.util.List;

public class CreateManufactureDayPlanFormStep3 extends BpmWizardFormStep {

    private BpmGrid grid;

    public CreateManufactureDayPlanFormStep3(BpmWizardForm wizardForm) {
        super(CreateManufactureDayPlanFormStep3.class.getSimpleName(), wizardForm);
    }

    @Override
    public void init() {
        Label titleLabel = new Label("Выполнение заданий по линиям");
        titleLabel.setStyleName(BpmThemeStyles.LABEL_H3);
        wizardFormStepLayout.addComponent(titleLabel);

        grid = new BpmGrid();
        grid.setWidth("700px");
        grid.setHeightMode(HeightMode.ROW);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setHeightByRows(7);
        grid.addGridColumn("manufactureDayPlanLineName", String.class, "Линия", 100);
        grid.addGridColumn("manufactureDayPlanLineManagerComment", String.class, "Комментарий сотрудника БЗКПД", 250);
        grid.addGridColumn("manufactureDayPlanLineEmployeeComment", String.class, "Комментарий мастера", 250);
        grid.addGridColumn("manufactureDayPlanLineStatus", String.class, "Статус", 100);

        BpmManufactureDayPlan manufactureDayPlan = BpmDaoFactory.getManufactureDayPlanDao().
                findOne((Long) getForm().getProcessVariable(BpmManufactureProcessModel.MANUFACTURE_DAY_PLAN_ID));
        List<BpmManufactureDayPlanLine> manufactureDayPlanLineList = BpmDaoFactory.getManufactureDayPlanLineDao().
                findByManufactureDayPlanOrderByManufactureLineNameAsc(manufactureDayPlan);

        for (BpmManufactureDayPlanLine manufactureDayPlanLine : manufactureDayPlanLineList) {
            grid.addItem(manufactureDayPlanLine);
            grid.setItemProperty(manufactureDayPlanLine, "manufactureDayPlanLineName", manufactureDayPlanLine.getManufactureLine().getName());
            grid.setItemProperty(manufactureDayPlanLine, "manufactureDayPlanLineManagerComment", manufactureDayPlanLine.getManagerComment());
            grid.setItemProperty(manufactureDayPlanLine, "manufactureDayPlanLineEmployeeComment", manufactureDayPlanLine.getEmployeeComment());
            grid.setItemProperty(manufactureDayPlanLine, "manufactureDayPlanLineStatus", manufactureDayPlanLine.getStatus());
        }

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public void select(SelectionEvent event) {
                BpmManufactureDayPlanLine planLine = (BpmManufactureDayPlanLine) grid.getSelectedRow();
                if(planLine == null)
                    return;
                BpmManufactureDayPlanLineEditor editor = new BpmManufactureDayPlanLineEditor(planLine);
                editor.show();
            }
        });

        wizardFormStepLayout.addComponent(grid);
    }

    private class BpmManufactureDayPlanLineEditor extends BpmWindow {
        private static final long serialVersionUID = 1L;
        
        BpmManufactureDayPlanLineEditor(BpmManufactureDayPlanLine manufactureDayPlanLine) {
            super("Задание по линии");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("900px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            TextField manufactureLineNameTextField = new TextField("Линия");
            manufactureLineNameTextField.setWidth("100%");
            manufactureLineNameTextField.setValue(manufactureDayPlanLine.getManufactureLine().getName());
            manufactureLineNameTextField.setReadOnly(true);
            layout.addComponent(manufactureLineNameTextField);

            TextArea managerCommentTextArea = new TextArea("Комментарий сотрудника БЗКПД");
            managerCommentTextArea.setWidth("100%");
            managerCommentTextArea.setRows(2);
            String managerComment = manufactureDayPlanLine.getManagerComment();
            managerCommentTextArea.setValue(managerComment == null ? "" : managerComment);
            managerCommentTextArea.setReadOnly(true);
            layout.addComponent(managerCommentTextArea);

            TextArea employeeCommentTextArea = new TextArea("Комментарий мастера");
            employeeCommentTextArea.setWidth("100%");
            employeeCommentTextArea.setRows(2);
            String employeeComment = manufactureDayPlanLine.getEmployeeComment();
            employeeCommentTextArea.setValue(employeeComment == null ? "" : employeeComment);
            employeeCommentTextArea.setReadOnly(true);
            layout.addComponent(employeeCommentTextArea);

            TextField statusTextField = new TextField("Статус");
            statusTextField.setWidth("100%");
            statusTextField.setValue(manufactureDayPlanLine.getStatus());
            statusTextField.setReadOnly(true);
            layout.addComponent(statusTextField);

            List<BpmManufactureDayPlanLineBuildingObjectItem> objectItems = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().findByManufactureDayPlanLineOrderByArticleIdAsc(manufactureDayPlanLine);
            ManufactureDayPlanLineBuildingObjectItemGrid itemsGrid = new ManufactureDayPlanLineBuildingObjectItemGrid();
            itemsGrid.loadData(objectItems);
            itemsGrid.setReadOnly(true);
            layout.addComponent(itemsGrid);
            
            Button editButton = new Button("Закрыть");
            editButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    close();
                }
            });
            layout.addComponent(editButton);
            layout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        }
    }
}
