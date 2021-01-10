package ru.everybit.bzkpd_bsk.model.manufacture;

import ru.everybit.bzkpd_bsk.model.BpmProcessModel;

public interface BpmManufactureProcessModel extends BpmProcessModel {
    final String MANUFACTURE_DAY_PLAN_LINE_ID = "manufactureDayPlanLineId";
    final String MANUFACTURE_DAY_PLAN_ID = "manufactureDayPlanId";
    final String MANUFACTURE_TRANSFER_TO_STOCK_ID = "manufactureTransferToStockId";
}
