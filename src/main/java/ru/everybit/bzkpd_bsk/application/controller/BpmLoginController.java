package ru.everybit.bzkpd_bsk.application.controller;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmPortal;

import java.util.List;

public class BpmLoginController extends VerticalLayout {

    private IdentityService identityService;

    public BpmLoginController() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        identityService = processEngine.getIdentityService();
        setSizeFull();
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);

        // Build form labels
        CssLayout labels = new CssLayout();
        Label welcome = new Label("Авторизация");
        welcome.setSizeUndefined();
        welcome.addStyleName(BpmThemeStyles.LABEL_H4);
        welcome.addStyleName(BpmThemeStyles.LABEL_COLORED);
        labels.addComponent(welcome);
        loginPanel.addComponent(labels);

        // Build from fields
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName(BpmThemeStyles.FIELDS);

        final TextField loginField = new TextField("Логин");
        loginField.setIcon(FontAwesome.USER);
        loginField.addStyleName(BpmThemeStyles.TEXTFIELD_INLINE_ICON);
        loginField.focus();

        final PasswordField passwordField = new PasswordField("Пароль");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.addStyleName(BpmThemeStyles.TEXTFIELD_INLINE_ICON);

        Button loginButton = new Button("Войти");
        loginButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        loginButton.setClickShortcut(KeyCode.ENTER);

        fields.addComponents(loginField, passwordField, loginButton);
        fields.setComponentAlignment(loginButton, Alignment.BOTTOM_LEFT);

        loginButton.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                String login = loginField.getValue();
                String password = passwordField.getValue();
                User user = handleLogin(login, password);
                if (user != null) {
                    BpmPortal bpmPortal = new BpmPortal();
                    BpmBuildingApplicationController controller = new BpmBuildingApplicationController(bpmPortal);
                    controller.init();
                    UI.getCurrent().setContent(bpmPortal);
                } else {
                    Notification notification = new Notification("Неправильное имя пользователя или пароль", Notification.Type.ERROR_MESSAGE);
                    notification.setDelayMsec(2000);
                    notification.setPosition(Position.BOTTOM_RIGHT);
                    notification.show(Page.getCurrent());
                }
            }
        });
        loginPanel.addComponent(fields);
        return loginPanel;
    }

    public User handleLogin(String login, String password) {
        boolean checkPassword = identityService.checkPassword(login, password);
        if (checkPassword) {
            User user = identityService.createUserQuery().userId(login).singleResult();
            BpmSessionService.setCurrentUser(user);

            List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
            BpmSessionService.setCurrentGroups(groups);

            return user;
        }
        return null;
    }
}
