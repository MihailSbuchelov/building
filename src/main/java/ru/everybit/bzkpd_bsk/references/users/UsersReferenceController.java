package ru.everybit.bzkpd_bsk.references.users;

import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;

public class UsersReferenceController {
    public static void init(BpmWorkingArea workingArea) {
        UsersReference usersReference = new UsersReference();
        usersReference.initView();
        workingArea.setContent(usersReference.getView());
    }
}
