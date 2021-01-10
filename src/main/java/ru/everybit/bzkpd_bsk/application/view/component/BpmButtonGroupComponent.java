package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BpmButtonGroupComponent implements Serializable {
    // local
    private Map<String, Button> menuButtons;
    private BpmButtonGroupComponentListener buttonGroupListener;
    private ClickListenerImpl buttonClickListener;
    // data
    private String firstButton;
    private String lastButton;
    private String selectedStyle;
    private AbstractLayout buttonsLayout;
    private String primaryStyle;

    public BpmButtonGroupComponent(AbstractLayout buttonsLayout, String primaryStyle, String selectedStyle) {
        this.buttonsLayout = buttonsLayout;
        this.primaryStyle = primaryStyle;
        this.selectedStyle = selectedStyle;

        buttonClickListener = new ClickListenerImpl();
        menuButtons = new HashMap<>();
        lastButton = null;
        firstButton = null;
    }

    public void setButtonGroupListener(BpmButtonGroupComponentListener buttonGroupListener) {
        this.buttonGroupListener = buttonGroupListener;
    }

    public void addButton(String id, String caption, int count, Resource icon, boolean doClick) {
        if (firstButton == null) {
            firstButton = id;
        }
        Button button = new Button();
        button.setId(id);
        if (caption != null) {
            button.setCaption(caption);
        }
        if (count > 0) {
            button.setCaption(button.getCaption() + " <span class=\"" + BpmThemeStyles.MENU_BADGE + "\">" + count + "</span>");
            button.setHtmlContentAllowed(true);
        }
        if (icon != null) {
            button.setIcon(icon);
        }
        if (primaryStyle != null) {
            button.setPrimaryStyleName(primaryStyle);
        }
        button.addClickListener(buttonClickListener);
        buttonsLayout.addComponent(button);
        menuButtons.put(id, button);
        if (doClick) {
            button.click();
        }
    }

    public void reset() {
        buttonsLayout.removeAllComponents();
        menuButtons.clear();
        lastButton = null;
        firstButton = null;
    }

    public void clickFirstButton() {
        doClick(firstButton);
    }

    public void doClick(String buttonId) {
        menuButtons.get(buttonId).click();
    }

    public AbstractLayout getUI() {
        return buttonsLayout;
    }

    private class ClickListenerImpl implements ClickListener {
        private static final long serialVersionUID = 8741368666968851803L;

        @Override
        public void buttonClick(ClickEvent event) {
            String id = event.getButton().getId();
            menuButtons.get(id).addStyleName(selectedStyle);
            if (lastButton != null) {
                menuButtons.get(lastButton).removeStyleName(selectedStyle);
            }
            lastButton = id;
            if (buttonGroupListener != null) {
                buttonGroupListener.buttonPressed(id);
            }
        }
    }

    public String getSelectedItemId() {
        return lastButton;
    }

    public interface BpmButtonGroupComponentListener extends Serializable {
        void buttonPressed(String buttonId);
    }
}