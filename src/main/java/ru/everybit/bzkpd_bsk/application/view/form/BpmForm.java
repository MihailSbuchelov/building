package ru.everybit.bzkpd_bsk.application.view.form;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import ru.everybit.bzkpd_bsk.application.controller.task.BpmMyTaskController;
import ru.everybit.bzkpd_bsk.application.service.business_log.BpmBusinessLogService;
import ru.everybit.bzkpd_bsk.application.service.case_management.BpmCaseManagementService;
import ru.everybit.bzkpd_bsk.application.service.process.BpmProcessService;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BpmForm implements Serializable {
    // ui
    private BpmWorkingArea workingArea;
    private VerticalLayout formLayout;
    protected VerticalLayout formContentLayout;
    protected HorizontalLayout buttonLayout;
    // data
    protected Task task;
    protected ProcessInstance processInstance;
    protected ProcessDefinition processDefinition;
    private boolean addSaveButton;

    public BpmForm(boolean addSaveButton) {
        this.addSaveButton = addSaveButton;
    }

    public BpmForm() {
        this(true);
    }

    public Component getUi() {
        return formLayout;
    }

    public void showForm(Task task, BpmWorkingArea workingArea) {
        this.task = task;
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        this.processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        this.processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        this.workingArea = workingArea;

        initUi();
        initModel();
    }

    protected void initUi() {
        formLayout = new VerticalLayout();
        formLayout.setHeight("100%");
        formLayout.setSpacing(true);

        HorizontalLayout headerLayout = createHeaderLayout();
        formLayout.addComponent(headerLayout);

        HorizontalLayout harnessContentLayout = createFormContentLayout();
        formLayout.addComponent(harnessContentLayout);
        formLayout.setExpandRatio(harnessContentLayout, 1);

        HorizontalLayout buttonLayout = createButtonLayout();
        formLayout.addComponent(buttonLayout);

        initUiContent();
    }

    protected HorizontalLayout createHeaderLayout() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        headerLayout.setHeightUndefined();

        Label titleLabel = new Label(task.getName());
        titleLabel.addStyleName(BpmThemeStyles.LABEL_H2);
        headerLayout.addComponent(titleLabel);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        headerLayout.addComponent(buttonLayout);
        headerLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);

        // Создать кнопку для просмотра истории по процессу
        Button historyButton = new Button(FontAwesome.HISTORY);
        historyButton.setDescription("История по процессу");
        historyButton.addStyleName(BpmThemeStyles.BUTTON_SMALL);
        historyButton.setSizeUndefined();
        historyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                BpmBusinessLogService.showLog(processInstance);
            }
        });
        buttonLayout.addComponent(historyButton);
        buttonLayout.setComponentAlignment(historyButton, Alignment.MIDDLE_RIGHT);
        return headerLayout;
    }

    protected HorizontalLayout createFormContentLayout() {
        HorizontalLayout formContentScrollableLayout = new HorizontalLayout();
        formContentScrollableLayout.setSizeFull();

        formContentLayout = new VerticalLayout();
        formContentLayout.setSpacing(true);
        formContentLayout.setSizeFull();
        formContentLayout.addStyleName(BpmThemeStyles.LAYOUT_SCROLLABLE);
        formContentScrollableLayout.addComponent(formContentLayout);
        formContentScrollableLayout.setExpandRatio(formContentLayout, 1);

        return formContentScrollableLayout;
    }

    protected HorizontalLayout createButtonLayout() {
        buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth("150px");

        Button submitButton = new Button("Завершить");
        submitButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        submitButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                submitButtonClick();
            }
        });
        buttonLayout.addComponent(submitButton);
        buttonLayout.setComponentAlignment(submitButton, Alignment.TOP_LEFT);

        if (addSaveButton) {
            Button saveButton = new Button("Сохранить", FontAwesome.SAVE);
            saveButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    saveButtonClick();
                }
            });
            buttonLayout.addComponent(saveButton, 0);
            buttonLayout.setComponentAlignment(saveButton, Alignment.TOP_LEFT);
            buttonLayout.setWidth("290px");
        }

        return buttonLayout;
    }

    /**
     * Инициализировать содержимое формы
     */
    protected void initUiContent() {
        TextArea textArea = new TextArea();
        textArea.setValue(task.getDescription() == null ? "" : task.getDescription());
        textArea.setReadOnly(true);
        textArea.setWidth("50%");
        formContentLayout.addComponent(textArea);
    }

    /**
     * Инициализировать содержимое формы значениями из контекста
     */
    protected void initModel() {
    }

    /**
     * Сохранить промежуточные переменные в контексте задачи
     */
    protected void saveButtonClick() {
    }

    /**
     * Сохранить переменные в контексте процесса и модели данных
     */
    protected void submitButtonClick() {
        if (BpmCaseManagementService.checkForSubCases(task)) {
            Notification notification = new Notification("Не удалось завершить задачу, так как есть связанные не завершенные процессы", Notification.Type.ERROR_MESSAGE);
            notification.setDelayMsec(2000);
            notification.setPosition(Position.BOTTOM_RIGHT);
            notification.show(Page.getCurrent());
            return;
        }
        BpmProcessService.completeTask(task);
        BpmMyTaskController.init(workingArea);
    }

    public void startSubProcess(String processDefinitionKey, String code) {
        BpmCaseManagementService.startSubProcess(processDefinitionKey, code, task);
    }

    public void startSubProcess(String processDefinitionKey, String code, Map<String, Object> variables) {
        BpmCaseManagementService.startSubProcess(processDefinitionKey, code, task, variables);
    }

    public void startSubProcess(String processDefinitionKey, String code, String variableName, Object variableValue) {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put(variableName, variableValue);
        BpmCaseManagementService.startSubProcess(processDefinitionKey, code, task, variables);
    }

    public Object getParentProcessVariable(String variableName) {
        return BpmCaseManagementService.getParentProcessVariable(task.getProcessInstanceId(), variableName);
    }

    public Object getProcessVariable(String variableName) {
        return BpmProcessService.getProcessVariable(task.getProcessInstanceId(), variableName);
    }

    public void setProcessVariable(String variableName, Object variableValue) {
        BpmProcessService.setProcessVariable(task.getProcessInstanceId(), variableName, variableValue);
    }

    public Object getTaskVariable(String variableName) {
        return BpmProcessService.getTaskVariable(task.getId(), variableName);
    }

    public void setTaskVariable(String variableName, Object variableValue) {
        BpmProcessService.setTaskVariable(task.getId(), variableName, variableValue);
    }
}