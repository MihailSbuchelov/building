package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class BpmEditButtons extends HorizontalLayout {
    // data
    private Object selectedItem;
    // view
    private Button addButton;
    private Button deleteButton;
    private Button editButton;

    public BpmEditButtons() {
        addButton = new Button(FontAwesome.PLUS);
        addComponent(addButton);

        deleteButton = new Button(FontAwesome.MINUS);
        deleteButton.setEnabled(false);
        addComponent(deleteButton);

        editButton = new Button(FontAwesome.EDIT);
        editButton.setEnabled(false);
        addComponent(editButton);
    }

    public void addButtonStyleName(String style) {
        addButton.addStyleName(style);
        deleteButton.addStyleName(style);
        editButton.addStyleName(style);
    }

    public void addAddListener(final AddListener addListener) {
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addListener.eventAdd();
            }
        });
    }

    public void addDeleteListener(final DeleteListener deleteListener) {
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                deleteListener.eventDelete(selectedItem);
            }
        });
    }

    public void addEditListener(final EditListener editListener) {
        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editListener.eventEdit(selectedItem);
            }
        });
    }

    public void selectItem(Object selectedItem) {
        this.selectedItem = selectedItem;
        boolean enabled = selectedItem != null;
        deleteButton.setEnabled(enabled);
        editButton.setEnabled(enabled);
    }

    public interface AddListener {
        void eventAdd();
    }

    public interface DeleteListener {
        void eventDelete(Object item);
    }

    public interface EditListener {
        void eventEdit(Object item);
    }
}
