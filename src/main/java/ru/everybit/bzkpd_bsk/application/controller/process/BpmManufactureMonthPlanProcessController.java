package ru.everybit.bzkpd_bsk.application.controller.process;

import java.util.HashMap;
import java.util.Map;

import ru.everybit.bzkpd_bsk.application.controller.task.BpmMyTaskController;
import ru.everybit.bzkpd_bsk.application.service.process.BpmProcessService;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;

public class BpmManufactureMonthPlanProcessController {
    public static void init(BpmWorkingArea workingArea) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmProcessModel.TYPE_VARIABLE, "План производства");
        BpmProcessService.startProcessByKey("ManufactureMonthPlanProcess", "ПМ", variables);
        BpmMyTaskController.init(workingArea);
    }
}
