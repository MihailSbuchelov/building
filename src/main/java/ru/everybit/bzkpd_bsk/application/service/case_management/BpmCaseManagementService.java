package ru.everybit.bzkpd_bsk.application.service.case_management;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.task.Task;
import ru.everybit.bzkpd_bsk.application.service.process.BpmProcessService;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;

import java.util.HashMap;
import java.util.Map;

public class BpmCaseManagementService {
    public static boolean checkForSubCases(Task task) {
        Integer count = (Integer) BpmProcessService.getTaskVariable(task.getId(), BpmProcessModel.CHILD_PROCESS_INSTANCE_COUNT);
        return count != null && count > 0;
    }

    public static void startSubProcess(String processDefinitionKey, String code, Task task, Map<String, Object> variables) {
        variables.put(BpmProcessModel.PARENT_PROCESS_INSTANCE_ID, task.getProcessInstanceId());
        variables.put(BpmProcessModel.PARENT_TASK_ID, task.getId());
        BpmProcessService.startProcessByKey(processDefinitionKey, code, variables);
        Integer count = (Integer) BpmProcessService.getTaskVariable(task.getId(), BpmProcessModel.CHILD_PROCESS_INSTANCE_COUNT);
        if (count == null) {
            count = 0;
        }
        BpmProcessService.setTaskVariable(task.getId(), BpmProcessModel.CHILD_PROCESS_INSTANCE_COUNT, count + 1);
    
    }
    public static void startSubProcess(String processDefinitionKey, String code, Task task) {
        Map<String, Object> variables = new HashMap<>(2);
        startSubProcess(processDefinitionKey, code, task, variables);
      }

    public static void notifyEndEvent(DelegateExecution execution) {
        String taskId = (String) BpmProcessService.getProcessVariable(execution.getProcessInstanceId(), BpmProcessModel.PARENT_TASK_ID);
        Integer count = (Integer) BpmProcessService.getTaskVariable(taskId, BpmProcessModel.CHILD_PROCESS_INSTANCE_COUNT);
        if (count != null && count > 0) {
            BpmProcessService.setTaskVariable(taskId, BpmProcessModel.CHILD_PROCESS_INSTANCE_COUNT, count - 1);
        }
    }

    public static Object getParentProcessVariable(String processInstanceId, String variableName) {
        String parentProcessInstanceId = (String) BpmProcessService.getProcessVariable(processInstanceId, BpmProcessModel.PARENT_PROCESS_INSTANCE_ID);
        Object variableValue = BpmProcessService.getProcessVariable(parentProcessInstanceId, variableName);
        if (variableValue == null) {
            return getParentProcessVariable(parentProcessInstanceId, variableName);
        }
        return variableValue;
    }
}
