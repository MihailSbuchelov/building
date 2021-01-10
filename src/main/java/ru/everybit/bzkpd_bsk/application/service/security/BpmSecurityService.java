package ru.everybit.bzkpd_bsk.application.service.security;

import org.camunda.bpm.engine.identity.Group;
import ru.everybit.bzkpd_bsk.application.service.session.BpmSessionService;

public class BpmSecurityService {
    public static boolean checkRoles(BpmRole... roles) {
        for (Group group : BpmSessionService.getCurrentUserGroups()) {
            for (BpmRole role : roles) {
                if (group.getId().equals(role.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
