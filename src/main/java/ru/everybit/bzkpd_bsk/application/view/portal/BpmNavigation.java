package ru.everybit.bzkpd_bsk.application.view.portal;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmButtonGroupComponent;

import java.io.Serializable;

public class BpmNavigation implements Serializable {
    private static final long serialVersionUID = -5861346813559149611L;
    // UI
    private VerticalLayout navigationPanel;
    // local
    private BpmButtonGroupComponent buttonGroup;

    public BpmNavigation() {
        buttonGroup = new BpmButtonGroupComponent(new VerticalLayout(), BpmThemeStyles.MENU_ITEM, BpmThemeStyles.SELECTED);

        navigationPanel = new VerticalLayout();
        navigationPanel.setWidth("250px");
        navigationPanel.setHeight("100%");
        navigationPanel.setStyleName(BpmThemeStyles.MENU_ROOT);
//        navigationPanel.addStyleName(BpmThemeStyles.LAYOUT_SCROLLABLE);

        buttonGroup.getUI().setPrimaryStyleName(BpmThemeStyles.MENU_ITEMS);
        navigationPanel.addComponent(buttonGroup.getUI());
        navigationPanel.setExpandRatio(buttonGroup.getUI(), 1);
    }

    public void setButtonGroupListener(BpmButtonGroupComponent.BpmButtonGroupComponentListener navigationListener) {
        buttonGroup.setButtonGroupListener(navigationListener);
    }

    public void reset() {
        buttonGroup.reset();
    }

    public void addSeparator(String title) {
        Label label = new Label(title, ContentMode.HTML);
        label.setPrimaryStyleName(BpmThemeStyles.MENU_SUBTITLE);
        label.addStyleName(BpmThemeStyles.LABEL_H4);
        label.setSizeUndefined();
        buttonGroup.getUI().addComponent(label);
    }

    public void addNavigationElement(String id, String title) {
        addNavigationElement(id, title, null);
    }

    public void addNavigationElement(String id, String title, int count, Resource icon) {
        buttonGroup.addButton(id, title, count, icon, false);
    }

    public void addNavigationElement(Class id, String title, Resource icon) {
        addNavigationElement(id.getSimpleName(), title, icon);
    }

    public void addNavigationElement(String id, String title, Resource icon) {
        buttonGroup.addButton(id, title, 0, icon, false);
    }

    public void navigateToTheFirstForm() {
        buttonGroup.clickFirstButton();
    }

    public void navigateTo(String id) {
        buttonGroup.doClick(id);
    }

    public AbstractLayout getUI() {
        return navigationPanel;
    }

    public String getSelectedItemId() {
        return buttonGroup.getSelectedItemId();
    }
}
