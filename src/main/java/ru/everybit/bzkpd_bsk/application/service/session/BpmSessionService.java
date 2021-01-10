package ru.everybit.bzkpd_bsk.application.service.session;

import com.vaadin.server.VaadinSession;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;

import java.util.ArrayList;
import java.util.List;

public class BpmSessionService {

    public static User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    }

    public static String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static void setCurrentUser(User user) {
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
    }

    public static List<Group> getCurrentUserGroups() {
        @SuppressWarnings("unchecked")
        List<Group> groups = (List<Group>) VaadinSession.getCurrent().getAttribute("currentUserGroups");
        if (groups == null) {
            return new ArrayList<>();
        }
        return groups;
    }

    public static List<String> getCurrentUserGroupIds() {
        @SuppressWarnings("unchecked")
        List<String> groupIds = (List<String>) VaadinSession.getCurrent().getAttribute("currentUserGroupIds");
        if (groupIds == null) {
            return new ArrayList<>();
        }
        return groupIds;
    }

    public static void setCurrentGroups(List<Group> groups) {
        VaadinSession.getCurrent().setAttribute("currentUserGroups", groups);

        if (groups.isEmpty()) {
            VaadinSession.getCurrent().setAttribute("currentUserGroupIds", null);
            return;
        }
        List<String> groupIds = new ArrayList<>();
        for (Group group : groups) {
            groupIds.add(group.getId());
        }
        VaadinSession.getCurrent().setAttribute("currentUserGroupIds", groupIds);
    }

}
