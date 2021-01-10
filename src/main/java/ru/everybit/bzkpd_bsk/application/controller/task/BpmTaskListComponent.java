package ru.everybit.bzkpd_bsk.application.controller.task;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;

import ru.everybit.bzkpd_bsk.application.service.form.BpmFormService;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmTable;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;

import java.util.ArrayList;
import java.util.List;

public class BpmTaskListComponent extends VerticalLayout {
    private static final long serialVersionUID = 1L;
    
    private final BeanItemContainer<Task> container;
    private Table table;
    private BpmWorkingArea workingArea;
    private String title;
    private TaskService taskService;
    private RuntimeService runtimeService;
    private IdentityService identityService;

    public BpmTaskListComponent(String title, List<Task> tasks, BpmWorkingArea workingArea, int pageLength) {
        this.title = title;
        this.container = new BeanItemContainer<>(Task.class, tasks);
        this.workingArea = workingArea;
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        taskService = processEngine.getTaskService();
        runtimeService = processEngine.getRuntimeService();
        identityService = processEngine.getIdentityService();
        buildUI(pageLength);
    }

    private void buildUI(int pageLength) {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidth("100%");

        Label titleLabel = new Label(title);
        titleLabel.setStyleName("h2");
        titleLayout.addComponent(titleLabel);
        titleLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout filterLayout = new HorizontalLayout();
        TextField searchField = new TextField();
        searchField.setIcon(FontAwesome.SEARCH);
        searchField.setInputPrompt("Поиск");
        searchField.addStyleName(BpmThemeStyles.TEXTFIELD_INLINE_ICON);
        searchField.setWidth("300px");
        filterLayout.addComponent(searchField);

        titleLayout.addComponent(filterLayout);
        titleLayout.setComponentAlignment(filterLayout, Alignment.MIDDLE_RIGHT);
        addComponent(titleLayout);

        table = new BpmTable();
        table.setPageLength(pageLength);
        table.setContainerDataSource(getContainer());

        table.addGeneratedColumn("#", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;
            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                final Task task = getContainer().getItem(itemId).getBean();
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId()).singleResult();
                String businessKey = processInstance.getBusinessKey();
                if (businessKey == null) {
                    businessKey = processInstance.getId();
                }
                return new Label(businessKey);
            }
        });

        table.addGeneratedColumn("name", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;
            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                BeanItem<Task> item = getContainer().getItem(itemId);
                final Task task = item.getBean();
                String currentUserId = BpmSessionService.getCurrentUserId();
                if (currentUserId.equals(task.getAssignee())) {
                    Button button = new Button(task.getName());
                    button.setStyleName(BpmThemeStyles.BUTTON_LINK);
                    button.addClickListener(new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;
                        @Override
                        public void buttonClick(ClickEvent event) {
                            BpmFormService.showFormForTask(task, workingArea);
                        }
                    });
                    return button;
                }
                return new Label(task.getName());
            }
        });

        table.addGeneratedColumn("role", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;
            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                BeanItem<Task> item = getContainer().getItem(itemId);
                String taskId = item.getBean().getId();

                // TODO Try extract to container filling, this may be work very slow
                List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
                VerticalLayout roleLayout = new VerticalLayout();

                List<String> taskCandidateGroupIds = new ArrayList<>();
                for (IdentityLink identityLink : identityLinks) {
                    if (identityLink.getGroupId() != null) {
                        taskCandidateGroupIds.add(identityLink.getGroupId());
                    }
                }
                if (taskCandidateGroupIds.size() > 0) {
                    List<Group> groups = identityService.createGroupQuery()
                            .groupIdIn(taskCandidateGroupIds.toArray(new String[taskCandidateGroupIds.size()])).list();
                    for (Group group : groups) {
                        roleLayout.addComponent(new Label(group.getName()));
                    }
                }
                return roleLayout;
            }
        });
        
        table.addGeneratedColumn("variable", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;
            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {
                BeanItem<Task> item = getContainer().getItem(itemId);
                String instanceId = item.getBean().getProcessInstanceId();
                String titleVariable = (String)runtimeService.getVariable(instanceId, BpmProcessModel.TITLE_VARIABLE);
                titleVariable = titleVariable == null ? "" : titleVariable;
                return new Label(titleVariable, ContentMode.HTML);
            }
        });
        
//        table.addGeneratedColumn("type", new ColumnGenerator() {
//            private static final long serialVersionUID = 1L;
//            @Override
//            public Object generateCell(final Table source, final Object itemId, Object columnId) {
//                BeanItem<Task> item = getContainer().getItem(itemId);
//                String instanceId = item.getBean().getProcessInstanceId();
//                String typeVariable = (String)runtimeService.getVariable(instanceId, BpmProcessModel.TYPE_VARIABLE);
//                typeVariable = typeVariable == null ? "" : typeVariable;
//                return new Label(typeVariable, ContentMode.HTML);
//            }
//        });
        
        searchField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void textChange(TextChangeEvent event) {
                getContainer().removeAllContainerFilters();
                getContainer().addContainerFilter(new SimpleStringFilter("name", event.getText(), true, false));
            }
        });
        table.setVisibleColumns("name", "variable", "createTime", "role", "assignee");
        table.setColumnHeaders("Задача", "", "Дата", "Роль", "Ответственный");
        table.setSortContainerPropertyId("createTime");
        table.setSortAscending(false);
        table.setWidth("100%");
        addComponent(getTable());
    }

    public Table getTable() {
        return table;
    }

    public BeanItemContainer<Task> getContainer() {
        return container;
    }
}
