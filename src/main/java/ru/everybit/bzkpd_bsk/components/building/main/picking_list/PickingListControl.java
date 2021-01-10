package ru.everybit.bzkpd_bsk.components.building.main.picking_list;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;
import ru.everybit.bzkpd_bsk.application.view.component.BpmControl;
import ru.everybit.bzkpd_bsk.application.view.component.BpmWindow;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObjectArticle;

import java.util.List;

public class PickingListControl implements BpmControl {
    // model
    private PickingListModel model;
    // view
    private PickingListView view;
    // control
    private PickingListProductsEditor pickingListProductsEditor;

    public void setModel(PickingListModel model) {
        this.model = model;
    }

    public void setView(PickingListView view) {
        this.view = view;
    }

    @Override
    public void initControl() {
        view.selectFirstArticleType();
        List<BpmBuildingObjectArticle> buildingObjectArticleList = model.getUsedDeletedBuildingObjectArticleList();
        if (!buildingObjectArticleList.isEmpty()) {
            String description = "Из справочника \"Номенклатура изделий\" удалены следующие элементы:\n";
            for (BpmBuildingObjectArticle buildingObjectArticle : buildingObjectArticleList) {
                description += " * " + buildingObjectArticle.getArticle().getArticleType().getName() + " ";
                description += buildingObjectArticle.getArticle().getName() + "\n";
                model.deleteBuildingObjectArticle(buildingObjectArticle);
            }
            Notification notification = new Notification("Удалены изделия", description, Notification.Type.WARNING_MESSAGE);
            notification.setDelayMsec(-1);
            notification.setPosition(Position.MIDDLE_CENTER);
            notification.show(Page.getCurrent());
        }
    }

    public void eventArticleTypeSelected(BpmArticleType articleType) {
        model.setArticleType(articleType);
        view.updateBuildingObjectArticle();
    }

    public void eventAddBuildingObjectArticle() {
        initPickingListProductsEditor();
        pickingListProductsEditor.editProducts(null);
    }

    public void eventEditBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        initPickingListProductsEditor();
        pickingListProductsEditor.editProducts(buildingObjectArticle);
    }

    public void eventDeleteBuildingObjectArticle(final BpmBuildingObjectArticle buildingObjectArticle) {
        final BpmWindow window = new BpmWindow("Удалить изделие " + buildingObjectArticle.getArticle().getName() + "?");

        Button okButton = new Button("Да");
        okButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventApproveDeleteBuildingObjectArticle(buildingObjectArticle);
                window.close();
            }
        });

        Button cancelButton = new Button("Нет");
        cancelButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window.close();
            }
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth("250px");
        layout.addComponent(okButton);
        layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
        window.setContent(layout);
        window.show();
    }

    private void initPickingListProductsEditor() {
        if (pickingListProductsEditor == null) {
            pickingListProductsEditor = new PickingListProductsEditor(model, this);
        }
    }

    public void updateBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        view.updateBuildingObjectArticle(buildingObjectArticle);
    }

    private void eventApproveDeleteBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        model.deleteBuildingObjectArticle(buildingObjectArticle);
        view.deleteBuildingObjectArticle(buildingObjectArticle);
    }
}
