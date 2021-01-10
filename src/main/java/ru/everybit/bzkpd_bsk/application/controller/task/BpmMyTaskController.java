package ru.everybit.bzkpd_bsk.application.controller.task;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import ru.everybit.bzkpd_bsk.application.service.form.BpmFormService;
import ru.everybit.bzkpd_bsk.application.service.process.BpmProcessService;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;

import java.util.List;

public class BpmMyTaskController {

    public static void init(final BpmWorkingArea workingArea) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final TaskService taskService = processEngine.getTaskService();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        List<Task> myTaskList = taskService.createTaskQuery()
                .taskAssignee(BpmSessionService.getCurrentUserId()).initializeFormKeys().list();
        BpmTaskListComponent myTaskListLayout = new BpmTaskListComponent("Мои задачи", myTaskList, workingArea, 7);
        layout.addComponent(myTaskListLayout);

        List<Task> myGroupTaskList = taskService.createTaskQuery()
                .taskCandidateGroupIn(BpmSessionService.getCurrentUserGroupIds()).initializeFormKeys().list();
        BpmTaskListComponent myGroupTaskLayout = new BpmTaskListComponent("Задачи моей рабочей группы", myGroupTaskList, workingArea, 6);
        Table table = myGroupTaskLayout.getTable();
        table.setVisibleColumns("name", "variable", "createTime", "role");
        table.setColumnHeaders("Задача", "", "Дата", "Роль");
        final BeanItemContainer<Task> container = myGroupTaskLayout.getContainer();
        table.addGeneratedColumn("Действие", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;
            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                BeanItem<Task> item = container.getItem(itemId);
                final Task task = item.getBean();
                Button button = new Button("Взять на себя");
                button.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        BpmProcessService.claimTask(task, BpmSessionService.getCurrentUserId());
                        BpmFormService.showFormForTask(task, workingArea);
                    }
                });
                return button;
            }
        });
        layout.addComponent(myGroupTaskLayout);

        workingArea.setContent(layout);
    }
}
