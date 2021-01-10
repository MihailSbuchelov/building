package ru.everybit.bzkpd_bsk.application;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.camunda.bpm.engine.identity.User;
import org.springframework.context.annotation.Scope;
import ru.everybit.bzkpd_bsk.application.controller.BpmBuildingApplicationController;
import ru.everybit.bzkpd_bsk.application.controller.BpmLoginController;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmPortal;

@Theme("bpm_building")
@org.springframework.stereotype.Component
@Scope("prototype")
public class BpmBuildingApplication extends UI {
    private static final long serialVersionUID = -8663387439830261740L;

    @Override
    protected void init(VaadinRequest request) {
        User user = BpmSessionService.getCurrentUser();
        if (user == null) {
            setContent(new BpmLoginController());
        } else {
            BpmPortal bpmPortal = new BpmPortal();
            BpmBuildingApplicationController controller = new BpmBuildingApplicationController(bpmPortal);
            controller.init();
            setContent(bpmPortal);
        }
    }
}
