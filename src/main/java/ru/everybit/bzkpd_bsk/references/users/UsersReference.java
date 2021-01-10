package ru.everybit.bzkpd_bsk.references.users;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import ru.everybit.bzkpd_bsk.application.service.security.BpmRole;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmEditButtons;
import ru.everybit.bzkpd_bsk.application.view.component.BpmGrid;
import ru.everybit.bzkpd_bsk.application.view.component.BpmView;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersReference implements BpmView {
    private VerticalLayout layout;
    private BpmGrid usersGrid;
    private BpmGrid groupsGrid;
    private IdentityService identityService;
    private BpmEditButtons editButtons;

    @Override
    public void initView() {
        identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();

        layout = new VerticalLayout();
        layout.setSpacing(true);

        Label titleLabel = new Label("Администрирование пользователей");
        titleLabel.addStyleName(BpmThemeStyles.LABEL_H2);
        layout.addComponent(titleLabel);

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.setWidth("100%");
        gridLayout.setSpacing(true);
        layout.addComponent(gridLayout);

        usersGrid = new BpmGrid();
        usersGrid.setWidth("100%");
        usersGrid.setCaption("Пользователи");
        usersGrid.addGridColumn("userId", String.class, "Логин");
        usersGrid.addGridColumn("password", String.class, "Пароль").setHidden(true);
        usersGrid.addGridColumn("fio", String.class, "ФИО");
        usersGrid.addGridColumn("email", String.class, "Почта");
        usersGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                editButtons.selectItem(usersGrid.getSelectedRow());
                updateGroupsGrid();
            }
        });
        gridLayout.addComponent(usersGrid);
        gridLayout.setExpandRatio(usersGrid, 1);

        groupsGrid = new BpmGrid();
        groupsGrid.setWidth("300px");
        groupsGrid.setCaption("Группы");
        groupsGrid.addGridColumn("groupName", String.class, "Название группы");
        groupsGrid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                updateUsersGrid();
            }
        });
        gridLayout.addComponent(groupsGrid);

        editButtons = new BpmEditButtons();
        editButtons.addAddListener(new BpmEditButtons.AddListener() {
            @Override
            public void eventAdd() {
                eventAddButtonClick();
            }
        });
        editButtons.addDeleteListener(new BpmEditButtons.DeleteListener() {
            @Override
            public void eventDelete(Object userId) {
                eventDeleteButtonClick((String) userId);
            }
        });
        editButtons.addEditListener(new BpmEditButtons.EditListener() {
            @Override
            public void eventEdit(Object userId) {
                eventEditButtonClick((String) userId);
            }
        });
        layout.addComponent(editButtons);

        updateUsersGrid();
        updateGroupsGrid();
    }

    private void updateUsersGrid() {
        usersGrid.removeAllItems();
        UserQuery userQuery = identityService.createUserQuery();
        if (groupsGrid.getSelectedRow() != null) {
            userQuery.memberOfGroup((String) groupsGrid.getSelectedRow());
        }
        for (User user : userQuery.orderByUserLastName().asc().orderByUserFirstName().asc().list()) {
            usersGrid.addItem(user.getId());
            usersGrid.setItemProperty(user.getId(), "userId", user.getId());
            usersGrid.setItemProperty(user.getId(), "password", user.getPassword());
            usersGrid.setItemProperty(user.getId(), "fio", user.getFirstName());
            usersGrid.setItemProperty(user.getId(), "email", user.getEmail());
        }
    }

    private void updateGroupsGrid() {
        groupsGrid.removeAllItems();
        GroupQuery groupQuery = identityService.createGroupQuery();
        if (usersGrid.getSelectedRow() != null) {
            groupQuery.groupMember((String) usersGrid.getSelectedRow());
        }
        for (Group group : groupQuery.orderByGroupName().asc().list()) {
            groupsGrid.addItem(group.getId());
            groupsGrid.setItemProperty(group.getId(), "groupName", group.getName());
        }
    }

    @Override
    public Component getView() {
        return layout;
    }

    private void eventAddButtonClick() {
        new Editor(null).show();
    }

    private void eventDeleteButtonClick(String userId) {
    }

    private void eventEditButtonClick(String userId) {
        new Editor(userId).show();
    }

    private class Editor extends BpmWindow {
        // model
        private String password;
        private boolean newUser;
        // view
        private TextField userIdTextField;
        private TextField fioTextField;
        private TextField emailTextField;
        private TextField passwordTextField;
        private TwinColSelect groupTwinColSelect;

        private Editor(String userId) {
            super(userId == null ? "Создание нового пользователя" : "Редактирование пользователя");

            newUser = userId == null;
            String fio = newUser ? null : (String) usersGrid.getItemProperty(userId, "fio");
            String email = newUser ? null : (String) usersGrid.getItemProperty(userId, "email");
            password = newUser ? null : (String) usersGrid.getItemProperty(userId, "password");

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("600px");
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            FormLayout formLayout = new FormLayout();
            formLayout.setWidth("100%");
            layout.addComponent(formLayout);

            userIdTextField = new TextField("Логин");
            userIdTextField.setWidth("100%");
            userIdTextField.setValue(userId == null ? "" : userId);
            userIdTextField.setReadOnly(!(userId == null));
            formLayout.addComponent(userIdTextField);

            fioTextField = new TextField("ФИО");
            fioTextField.setWidth("100%");
            fioTextField.setValue(fio == null ? "" : fio);
            formLayout.addComponent(fioTextField);

            emailTextField = new TextField("email");
            emailTextField.setWidth("100%");
            emailTextField.setValue(email == null ? "" : email);
            formLayout.addComponent(emailTextField);

            passwordTextField = new TextField("Пароль");
            passwordTextField.setWidth("100%");
            formLayout.addComponent(passwordTextField);

            groupTwinColSelect = new TwinColSelect("Группы");
            groupTwinColSelect.setWidth("100%");
            GroupQuery groupQuery = identityService.createGroupQuery();
            for (Group group : groupQuery.orderByGroupName().asc().list()) {
                groupTwinColSelect.addItem(group.getName());
            }
            if (userId != null) {
                List<Group> groupList = identityService.createGroupQuery().groupMember(userId).orderByGroupName().asc().list();
                List<String> groupNameList = new ArrayList<>(groupList.size());
                for (Group group : groupList) {
                    groupNameList.add(group.getName());
                }
                groupTwinColSelect.setValue(groupNameList);
            }
            groupTwinColSelect.setReadOnly(BpmRole.ADMIN.getId().equals(userId));
            layout.addComponent(groupTwinColSelect);

            Button editorButton = new Button(userId == null ? "Создать" : "Изменить");
            editorButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
            editorButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    eventEditorButtonClick();
                }
            });
            layout.addComponent(editorButton);
            layout.setComponentAlignment(editorButton, Alignment.MIDDLE_CENTER);
        }

        private void eventEditorButtonClick() {
            userIdTextField.addValidator(new StringLengthValidator("Обязательное поле", 1, 1000, false));
            try {
                userIdTextField.validate();
            } catch (Validator.InvalidValueException e) {
                return;
            }
            String userId = userIdTextField.getValue();
            if (newUser) {
                User user = identityService.createUserQuery().userId(userId).singleResult();
                if (user != null) {
                    Notification notification = new Notification("Пользователь с именем " + userId + " уже создан", Notification.Type.ERROR_MESSAGE);
                    notification.setDelayMsec(2000);
                    notification.setPosition(Position.BOTTOM_RIGHT);
                    notification.show(Page.getCurrent());
                    return;
                }
            }
            String fio = fioTextField.getValue();
            emailTextField.addValidator(new EmailValidator("Ошибка в заполнении поля"));
            try {
                emailTextField.validate();
            } catch (Validator.InvalidValueException e) {
                return;
            }
            String email = emailTextField.getValue();
            boolean updatePassword = false;
            if (password == null) {
                passwordTextField.addValidator(new StringLengthValidator("Обязательное поле", 1, 32, false));
                try {
                    passwordTextField.validate();
                } catch (Validator.InvalidValueException e) {
                    return;
                }
                password = passwordTextField.getValue();
                updatePassword = true;
            } else {
                if (!"".equals(passwordTextField.getValue())) {
                    password = passwordTextField.getValue();
                    updatePassword = true;
                }
            }
            groupTwinColSelect.addValidator(new Validator() {
                @Override
                public void validate(Object value) throws Validator.InvalidValueException {
                    if (((Collection) value).isEmpty()) {
                        throw new InvalidValueException("Пользователь должен входить хотя бы в одну группу");
                    }
                }
            });
            try {
                groupTwinColSelect.validate();
            } catch (Validator.InvalidValueException e) {
                return;
            }

            User user = identityService.createUserQuery().userId(userId).singleResult();
            if (user == null) {
                user = new UserEntity(userId);
            } else {
                for (Group group : identityService.createGroupQuery().groupMember(userId).list()) {
                    identityService.deleteMembership(userId, group.getId());
                }
            }
            user.setFirstName(fio);
            user.setEmail(email);
            if (updatePassword) {
                user.setPassword(password);
            }
            identityService.saveUser(user);
            for (Object itemId : (Collection) groupTwinColSelect.getValue()) {
                String groupName = (String) itemId;
                Group group = identityService.createGroupQuery().groupName(groupName).singleResult();
                identityService.createMembership(userId, group.getId());
            }

            if (groupsGrid.getSelectedRow() != null) {
                groupsGrid.deselect(groupsGrid.getSelectedRow());
            }
            updateUsersGrid();
            if (usersGrid.getSelectedRow() != null) {
                usersGrid.deselect(usersGrid.getSelectedRow());
            }
            usersGrid.select(userId);
            close();
        }
    }
}
