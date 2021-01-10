package ru.everybit.bzkpd_bsk.application.controller.report;

import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.report.threed.BpmBuildingThreeDReport;

public class BpmThreeDReportController {

    public static void init(final BpmWorkingArea workingArea) {
        List<BpmBuildingObject> buildingObjects = BpmDaoFactory.getBuildingObjectDao().findByModelAttachmentNotNull();
//        BpmBuildingObjectsTableComponent bpmBuildingObjectsTableComponent = new BpmBuildingObjectsTableComponent(buildingObjects);
        VerticalLayout areaLayout = new VerticalLayout();
        
        Label headerLabel = new Label("3D Отчет");
        headerLabel.addStyleName(ValoTheme.LABEL_H2);
        areaLayout.addComponent(headerLabel);
        
        final BeanItemContainer<BpmBuildingObject> container = new BeanItemContainer<BpmBuildingObject>(BpmBuildingObject.class, buildingObjects);
        
        Grid buildingGrid = new Grid(container);
        buildingGrid.setWidth("100%");
        
        buildingGrid.setColumns("object", "projectOffice", "address");
        
        buildingGrid.getColumn("object").setHeaderCaption("Объект");
        buildingGrid.getColumn("projectOffice").setHeaderCaption("Проектный офис");
        buildingGrid.getColumn("address").setHeaderCaption("Адрес");
        
        areaLayout.addComponent(buildingGrid);
        
        buildingGrid.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                
                BeanItem<BpmBuildingObject> item = container.getItem(itemClickEvent.getItemId());
                BpmBuildingObject buildingObject = item.getBean();
                
                BpmBuildingThreeDReport buildingReport = new BpmBuildingThreeDReport(buildingObject);
                workingArea.setContent(buildingReport);
            }
        });
        
        workingArea.setContent(areaLayout);
    }
}
