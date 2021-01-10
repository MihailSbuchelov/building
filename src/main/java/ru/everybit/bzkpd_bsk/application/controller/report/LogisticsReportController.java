package ru.everybit.bzkpd_bsk.application.controller.report;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.report.logistics.LocalStockItemReport;
import ru.everybit.bzkpd_bsk.report.logistics.ShippingReport;
import ru.everybit.bzkpd_bsk.report.logistics.StockItemReport;

public class LogisticsReportController {
    public static void init(BpmWorkingArea workingArea) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        workingArea.setContent(layout);

        Label label = new Label("Отчет по складам");
        label.addStyleName(BpmThemeStyles.LABEL_H2);
        layout.addComponent(label);

        StockItemReport stockItemReport = new StockItemReport();
        stockItemReport.initView();
        layout.addComponent(stockItemReport.getView());

        LocalStockItemReport localStockItemReport = new LocalStockItemReport();
        localStockItemReport.initView();
        layout.addComponent(localStockItemReport.getView());

        ShippingReport shippingReport = new ShippingReport();
        shippingReport.initView();
        layout.addComponent(shippingReport.getView());
    }
}
