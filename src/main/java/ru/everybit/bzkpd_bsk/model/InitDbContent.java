package ru.everybit.bzkpd_bsk.model;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.everybit.bzkpd_bsk.application.service.security.BpmRole;

import javax.annotation.PostConstruct;

@Component
public class InitDbContent {
    @Autowired
    private IdentityService identityService;

    @PostConstruct
    public void init() {
        for (BpmRole role : BpmRole.values()) {
            Group group = identityService.createGroupQuery().groupId(role.getId()).singleResult();
            if (group == null) {
                group = identityService.newGroup(role.getId());
                group.setName(role.getName());
                identityService.saveGroup(group);
            }
        }
        User admin = identityService.createUserQuery().userId(BpmRole.ADMIN.getId()).singleResult();
        if (admin == null) {
            admin = identityService.newUser(BpmRole.ADMIN.getId());
            admin.setPassword(BpmRole.ADMIN.getId());
            identityService.saveUser(admin);
            identityService.createMembership(BpmRole.ADMIN.getId(), BpmRole.ADMIN.getId());
        }
    }
}
