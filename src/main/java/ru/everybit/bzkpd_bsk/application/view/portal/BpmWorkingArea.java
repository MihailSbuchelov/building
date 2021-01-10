package ru.everybit.bzkpd_bsk.application.view.portal;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;

public class BpmWorkingArea {
    private VerticalLayout layout;
    private VerticalLayout layoutContent;

    public BpmWorkingArea() {
        buildUi();
    }

    private void buildUi() {
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addStyleName(BpmThemeStyles.LAYOUT_SCROLLABLE);

        layoutContent = new VerticalLayout();
        layoutContent.setMargin(new MarginInfo(false, true, true, true));
        layoutContent.setWidth("100%");
        layout.addComponent(layoutContent);
    }

    public Component getUi() {
        return layout;
    }

    public void setContent(Component component) {
        clearContent();
        layoutContent.addComponent(component);
    }

    public void clearContent() {
        layoutContent.removeAllComponents();
    }
}
