package ru.everybit.bzkpd_bsk.report.threed;


import java.util.ArrayList;
import java.util.List;

import ru.everybit.bzkpd_bsk.application.view.component.BpmThreeD;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.components.building.BpmBuildingObjectLayout;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmProduct;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmSector;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BpmBuildingThreeDReport extends VerticalLayout{
    private static final long serialVersionUID = 1L;

    public BpmBuildingThreeDReport(BpmBuildingObject buildingObject) {
        super();

        Label titleLabel = new Label("3D - Отчет");
        titleLabel.addStyleName(ValoTheme.LABEL_H2);
        addComponent(titleLabel);
        
        
        BpmBuildingObjectLayout buildingObjectLayout = new BpmBuildingObjectLayout(buildingObject);
        addComponent(buildingObjectLayout);
        
        BpmThreeD threeD = new BpmThreeD("report-3d", buildingObject.getId());
        
        List<BpmBuildingPlanItem> buildingPlanItems = BpmDaoFactory.getBuildingPlanItemDao().findByBuildingObjectId(buildingObject.getId());
        List<BpmThreeD.PlanItemDTO> items = new ArrayList<BpmThreeD.PlanItemDTO>();
        for (BpmBuildingPlanItem bpmBuildingPlanItem : buildingPlanItems) {
            BpmProduct product = bpmBuildingPlanItem.getProduct();
            BpmSector sector = product.getSector();
            BpmArticle article = product.getBuildingObjectArticle().getArticle();
            String name = article.getName();
            String articleType = article.getArticleType().getName();
            String block = sector.getBlock().getName();
            String level = sector.getFloor().getName();
            
            
            items.add(new BpmThreeD.PlanItemDTO(name,
                                                articleType,
                                                level,
                                                block, 
                                                bpmBuildingPlanItem.getQuantity(),
                                                bpmBuildingPlanItem.getStockQuantity(),
                                                bpmBuildingPlanItem.getLocalStockQuantity())
                                                );
        }
        threeD.loadScene(buildingObject.getModelAttachment().getId(), items);
        addComponent(threeD);
    }
}
