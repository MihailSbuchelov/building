package ru.everybit.bzkpd_bsk.application.view.portal;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import ru.everybit.bzkpd_bsk.application.controller.BpmLoginController;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;

public class BpmPortal extends VerticalLayout {
    private Component navigationContent;
    private Component workingAreaContent;

    public void setNavigationContent(Component navigationContent) {
        this.navigationContent = navigationContent;
    }

    public void setWorkingAreaContent(Component ui) {
        this.workingAreaContent = ui;
    }

    public void buildUI() {
        setSizeFull();

        // headerLayout
        AbstractLayout headerLayout = buildHeaderLayout();
        addComponent(headerLayout);

        // centerLayout
        AbstractLayout centerLayout = buildCentralLayout();
        addComponent(centerLayout);
        setExpandRatio(centerLayout, 1.0f);
    }

    private AbstractLayout buildHeaderLayout() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth(100.0f, Unit.PERCENTAGE);
        headerLayout.addStyleName(BpmThemeStyles.MENU_TITLE);

        HorizontalLayout leftLayout = createHeaderLeftLayout();
        headerLayout.addComponent(leftLayout);
        headerLayout.setComponentAlignment(leftLayout, Alignment.MIDDLE_LEFT);

        HorizontalLayout centerLayout = createHeaderCenterLayout();
        headerLayout.addComponent(centerLayout);
        headerLayout.setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);
        headerLayout.setExpandRatio(centerLayout, 1.0f);

        HorizontalLayout rightLayout = createHeaderRightLayout();
        headerLayout.addComponent(rightLayout);
        headerLayout.setComponentAlignment(rightLayout, Alignment.MIDDLE_CENTER);

        return headerLayout;
    }

    private HorizontalLayout createHeaderLeftLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        Label applicationNameLabel = new Label("Система управления проектами строительства");
        applicationNameLabel.setStyleName(BpmThemeStyles.LABEL_BOLD);
        applicationNameLabel.setSizeUndefined();
        layout.addComponent(applicationNameLabel);
        layout.setComponentAlignment(applicationNameLabel, Alignment.MIDDLE_LEFT);

        return layout;
    }

    private HorizontalLayout createHeaderCenterLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        Label companyNameLabel = new Label("БЗКПД - БСК");
        companyNameLabel.setStyleName(BpmThemeStyles.LABEL_BOLD);
        companyNameLabel.setSizeUndefined();
        layout.addComponent(companyNameLabel);
        layout.setComponentAlignment(companyNameLabel, Alignment.MIDDLE_LEFT);

        Page.getCurrent().setTitle(companyNameLabel.getCaption());

        return layout;
    }

    private HorizontalLayout createHeaderRightLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        String groupName = BpmSessionService.getCurrentUserGroups().get(0).getName();
        String userName = BpmSessionService.getCurrentUser().getId();
        Label userNameLabel = new Label(groupName + " " + userName);
        userNameLabel.setSizeUndefined();
        layout.addComponent(userNameLabel);
        layout.setComponentAlignment(userNameLabel, Alignment.MIDDLE_RIGHT);

        Button logoutButton = new Button("Выход из системы");
        logoutButton.addStyleName(BpmThemeStyles.BUTTON_LOGOUT);
        logoutButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                BpmSessionService.setCurrentUser(null);
                UI.getCurrent().setContent(new BpmLoginController());
            }
        });
        layout.addComponent(logoutButton);

        return layout;
    }

    private HorizontalLayout buildCentralLayout() {
        VerticalLayout workAreaLayout = new VerticalLayout();
        workAreaLayout.setSizeFull();

        if (workingAreaContent != null) {
            workAreaLayout.addComponent(workingAreaContent);
            workAreaLayout.setExpandRatio(workingAreaContent, 1);
        }

        HorizontalLayout centerPanel = new HorizontalLayout();
        centerPanel.setSizeFull();
        centerPanel.addComponent(navigationContent);
        centerPanel.addComponent(workAreaLayout);
        centerPanel.setExpandRatio(workAreaLayout, 1);

        return centerPanel;
    }
}
