package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class BpmWindow extends Window {

    public BpmWindow() {
        init();
    }

    public BpmWindow(String caption) {
        super(caption);
        init();
    }

    private void init() {
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
        setModal(true);
        setResizable(false);
        center();
    }

    public void showMaximized() {
        setHeight((int) (UI.getCurrent().getPage().getBrowserWindowHeight() * 0.9), Sizeable.Unit.PIXELS);
        show();
    }

    public void show() {
        UI.getCurrent().addWindow(this);
        focus();
    }
}
