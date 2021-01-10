package ru.everybit.bzkpd_bsk.application.service.business_log;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.everybit.bzkpd_bsk.application.service.business_log.data.BpmBusinessLog;
import ru.everybit.bzkpd_bsk.application.service.business_log.data.BpmBusinessLogDao;
import ru.everybit.bzkpd_bsk.application.view.component.BpmTable;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;

import java.util.Date;
import java.util.List;

@Component
public class BpmBusinessLogService implements ApplicationContextAware {
    private static BpmBusinessLogDao businessLogDao;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        businessLogDao = applicationContext.getBean(BpmBusinessLogDao.class);
    }

    public static void log(String processInstanceId, String caption) {
        log(processInstanceId, caption, caption);
    }

    public static void log(String processInstanceId, String caption, String message) {
        BpmBusinessLog businessLog = new BpmBusinessLog();
        businessLog.setProcessInstanceId(processInstanceId);
        businessLog.setMessageTime(new Date());
        businessLog.setMessage(message);
        businessLogDao.save(businessLog);

        Notification.show(caption, Notification.Type.TRAY_NOTIFICATION);
    }

    public static void showLog(ProcessInstance processInstance) {
        BpmWindow window = new BpmWindow("История изменений по процессу " + processInstance.getBusinessKey());
        VerticalLayout logLayout = createLogLayout(processInstance);
        window.setContent(logLayout);
        window.showMaximized();
    }

    private static VerticalLayout createLogLayout(ProcessInstance processInstance) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        BpmTable logTable = createLogTable(processInstance);
        layout.addComponent(logTable);
        return layout;
    }

    private static BpmTable createLogTable(ProcessInstance processInstance) {
        BpmTable table = new BpmTable();
        List<BpmBusinessLog> businessLogList = businessLogDao.findByProcessInstanceIdOrderByMessageTimeAsc(processInstance.getProcessInstanceId());
        BeanItemContainer<BpmBusinessLog> container = new BeanItemContainer<>(BpmBusinessLog.class, businessLogList);
        table.setContainerDataSource(container);
        table.setVisibleColumns("messageTime", "message");
        table.setColumnHeaders("Дата", "Сообщение");
        return table;
    }
}
