package ru.everybit.bzkpd_bsk.application.service.case_management;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class BpmCaseManagementEndListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        BpmCaseManagementService.notifyEndEvent(execution);
    }
}
