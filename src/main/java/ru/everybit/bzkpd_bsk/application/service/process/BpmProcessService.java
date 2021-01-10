package ru.everybit.bzkpd_bsk.application.service.process;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.everybit.bzkpd_bsk.application.service.business_log.BpmBusinessLogService;
import ru.everybit.bzkpd_bsk.application.service.process.data.BpmProcessCode;
import ru.everybit.bzkpd_bsk.application.service.process.data.BpmProcessCodeDao;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;

import java.util.Map;

@Component
public class BpmProcessService implements ApplicationContextAware {
    private static BpmProcessCodeDao processCodeRepository;
    private static RuntimeService runtimeService;
    private static TaskService taskService;
    private static IdentityService identityService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        processCodeRepository = applicationContext.getBean(BpmProcessCodeDao.class);
        runtimeService = applicationContext.getBean(RuntimeService.class);
        taskService = applicationContext.getBean(TaskService.class);
        identityService = applicationContext.getBean(IdentityService.class);
    }

//    public static void startProcessByKey(String processDefinitionKey, String code) {
//        startProcessByKey(processDefinitionKey, code, null);
//    }

    public static void startProcessByKey(String processDefinitionKey, String code, Map<String, Object> variables) {
        String processCode = generateProcessCode(code).toString();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, processCode, variables);
        User user = BpmSessionService.getCurrentUser();
        BpmBusinessLogService.log(processInstance.getProcessInstanceId(),
                "Пользователь " + user.getId() + " запустил бизнес процесс \"" + processInstance.getBusinessKey() + "\"");
    }

    private static synchronized BpmProcessCode generateProcessCode(String code) {
        BpmProcessCode processCode = processCodeRepository.findByCode(code);
        if (processCode == null) {
            processCode = new BpmProcessCode();
            processCode.setCode(code);
            processCode.setNum(0l);
        }
        Long newNum = processCode.getNum() + 1l;
        processCode.setNum(newNum);
        return processCodeRepository.save(processCode);
    }

    public static void setProcessVariable(String processInstanceId, String variableName, Object variableValue) {
        runtimeService.setVariable(processInstanceId, variableName, variableValue);
    }

    public static Object getProcessVariable(String processInstanceId, String variableName) {
        return runtimeService.getVariable(processInstanceId, variableName);
    }

    public static void setTaskVariable(String taskId, String variableName, Object variableValue) {
        taskService.setVariableLocal(taskId, variableName, variableValue);
    }

    public static Object getTaskVariable(String taskId, String variableName) {
        return taskService.getVariableLocal(taskId, variableName);
    }

    public static void claimTask(Task task, String userId) {
        taskService.claim(task.getId(), userId);
        User user = identityService.createUserQuery().userId(userId).singleResult();
        BpmBusinessLogService.log(task.getProcessInstanceId(),
                "Пользователь " + user.getId() + " назначен исполнителем для задачи \"" + task.getName() + "\"");
    }

    public static void completeTask(Task task) {
        User user = BpmSessionService.getCurrentUser();
        BpmBusinessLogService.log(task.getProcessInstanceId(),
                "Пользователь " + user.getId() + " завершил задачу \"" + task.getName() + "\"");
        taskService.complete(task.getId());
    }
}
