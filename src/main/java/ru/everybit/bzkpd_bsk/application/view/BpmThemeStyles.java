package ru.everybit.bzkpd_bsk.application.view;

import com.vaadin.ui.themes.ValoTheme;

public interface BpmThemeStyles {
    // label
    String LABEL_H2 = ValoTheme.LABEL_H2;
    String LABEL_H3 = ValoTheme.LABEL_H3;
    String LABEL_H4 = ValoTheme.LABEL_H4;
    String LABEL_COLORED = ValoTheme.LABEL_COLORED;
    String LABEL_BOLD = ValoTheme.LABEL_BOLD;

    // text field
    String TEXTFIELD_INLINE_ICON = ValoTheme.TEXTFIELD_INLINE_ICON;

    // button
    String BUTTON_PRIMARY = ValoTheme.BUTTON_PRIMARY;
    String BUTTON_TINY = ValoTheme.BUTTON_TINY;
    String BUTTON_SMALL = ValoTheme.BUTTON_SMALL;
    String BUTTON_LINK = ValoTheme.BUTTON_LINK;
    String BUTTON_LOGOUT = BpmThemeStyles.BUTTON_LINK + " " + BpmThemeStyles.BUTTON_SMALL + " bpm-logout-button";
    String BUTTON_BORDERLESS = "borderless";

    // combo box
    String COMBO_BOX_BORDERLESS = "borderless";

    // layout
    String LAYOUT_SCROLLABLE = "v-scrollable";
    String LAYOUT_FORM_LIGHT = ValoTheme.FORMLAYOUT_LIGHT;

    // menu
    String MENU_ROOT = ValoTheme.MENU_ROOT;
    String MENU_TITLE = ValoTheme.MENU_TITLE;
    String MENU_SUBTITLE = ValoTheme.MENU_SUBTITLE;
    String MENU_BADGE = ValoTheme.MENU_BADGE;
    String MENU_ITEMS = "valo-menuitems";
    String MENU_ITEM = ValoTheme.MENU_ITEM;

    // common
    String SELECTED = "selected";
    String FIELDS = "fields";
}
