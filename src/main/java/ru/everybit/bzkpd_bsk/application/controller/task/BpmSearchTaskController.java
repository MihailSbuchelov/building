package ru.everybit.bzkpd_bsk.application.controller.task;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;

import java.util.List;

public class BpmSearchTaskController {

    public static void init(BpmWorkingArea workingArea) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> myTasks = taskService.createTaskQuery().initializeFormKeys().list();
        BpmTaskListComponent taskListLayout = new BpmTaskListComponent("Поиск задач", myTasks, workingArea, 17);
        workingArea.setContent(taskListLayout);
    }
}
