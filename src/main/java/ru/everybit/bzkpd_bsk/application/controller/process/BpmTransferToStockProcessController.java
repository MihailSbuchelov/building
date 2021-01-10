package ru.everybit.bzkpd_bsk.application.controller.process;

import java.util.HashMap;
import java.util.Map;

import ru.everybit.bzkpd_bsk.application.controller.task.BpmMyTaskController;
import ru.everybit.bzkpd_bsk.application.service.process.BpmProcessService;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.BpmProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureProcessModel;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStock;

public class BpmTransferToStockProcessController {

    public static void init(final BpmWorkingArea workingArea) {
        BpmTransferToStock transferToStock = new BpmTransferToStock();
        transferToStock.setStatus(BpmTransferToStock.STATUS_NEW);
        BpmDaoFactory.getTransferToStockDao().save(transferToStock);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmManufactureProcessModel.MANUFACTURE_TRANSFER_TO_STOCK_ID, transferToStock.getId());
        variables.put(BpmProcessModel.TYPE_VARIABLE, "План строительства");
        BpmProcessService.startProcessByKey("TransferToStockProcess", "ПДНС", variables);
        BpmMyTaskController.init(workingArea);
      
    }
    

}
