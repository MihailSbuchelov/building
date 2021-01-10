package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import ru.everybit.bzkpd_bsk.application.service.attachment.BpmAttachmentService;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBlock;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmFloor;

import java.io.IOException;
import java.util.List;

import net.minidev.json.JSONArray;

@com.vaadin.annotations.JavaScript({
        "vaadin://js/app.js?version=1.1.1",
        "vaadin://js/threejs/three.min.js",
        "vaadin://js/threejs/Detector.js",
        "vaadin://js/threejs/OrbitControls.js?version=1.1.1"})
public class BpmThreeD extends VerticalLayout {
    private static final long serialVersionUID = 1L;
    public final JavaScript javaScript;

    public BpmThreeD(String componentId, Long buildingObjectId) {
        super();
        
        Label modelComponent = new Label(String.format("<div id='%s'></div>", componentId), ContentMode.HTML);
        HorizontalLayout threedLevelLayout = new HorizontalLayout(modelComponent);
        threedLevelLayout.setSpacing(true);
        
        // Добавляем фильтры по этажам
        VerticalLayout levelButtonLayout = new VerticalLayout();
        levelButtonLayout.addComponent(new Label("Этажи"));
        List<BpmFloor> floors = BpmDaoFactory.getFloorDao().findByBuildingObjectIdOrderByPriorityDesc(buildingObjectId);
        for (final BpmFloor bpmFloor : floors) {
            final Button floorButton = new Button(bpmFloor.getName());
            floorButton.setWidth("130px");
            levelButtonLayout.addComponent(floorButton);
            floorButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    if(ValoTheme.BUTTON_PRIMARY.equals(floorButton.getStyleName())){
                        floorButton.removeStyleName(ValoTheme.BUTTON_PRIMARY);
                    }else {
                        floorButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
                    }
                    javaScript.execute(String.format("toogleLevel('%s')", bpmFloor.getName()));
                }
            });
        }
        
        levelButtonLayout.addComponent(new Label("Типы изделий"));
        // Добавляем фильтры по типам изделия
//        VerticalLayout articleTypeButtonLayout = new VerticalLayout();
        List<BpmArticleType> articleTypes = BpmDaoFactory.getArticleTypeDao().findByBuildingObjectId(buildingObjectId);
        for (final BpmArticleType articleType : articleTypes) {
            final Button articleTypeButton = new Button(articleType.getName());
            articleTypeButton.setWidth("130px");
            articleTypeButton.addStyleName("no-height");
//            articleTypeButtonLayout.addComponent(articleTypeButton);
            levelButtonLayout.addComponent(articleTypeButton);
            articleTypeButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    if(articleTypeButton.getStyleName().contains(ValoTheme.BUTTON_PRIMARY)){
                        articleTypeButton.removeStyleName(ValoTheme.BUTTON_PRIMARY);
                    }else {
                        articleTypeButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
                    }
                    javaScript.execute(String.format("toogleArticleType('%s')", articleType.getName()));
                }
            });
        }
        
        
        // Добавляем фильры по блок-секциям
        HorizontalLayout blockButtonLayout = new HorizontalLayout();
        List<BpmBlock> blocks = BpmDaoFactory.getBlockDao().findByBuildingObjectIdOrderByPriorityAsc(buildingObjectId);
        for (final BpmBlock bpmBlock : blocks) {
            final Button blockButton = new Button(bpmBlock.getName());
            blockButton.setWidth("120px");
            blockButtonLayout.addComponent(blockButton);
            blockButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    if(ValoTheme.BUTTON_PRIMARY.equals(blockButton.getStyleName())){
                        blockButton.removeStyleName(ValoTheme.BUTTON_PRIMARY);
                    }else {
                        blockButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
                    }
                    javaScript.execute(String.format("toogleBlock('%s')", bpmBlock.getName()));
                }
            });
        }
        
        threedLevelLayout.addComponent(levelButtonLayout);
//        threedLevelLayout.addComponent(articleTypeButtonLayout);
        addComponent(threedLevelLayout);
        addComponent(blockButtonLayout);
        
        VaadinSession.getCurrent().addRequestHandler(
                new RequestHandler() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean handleRequest(VaadinSession session,
                                                 VaadinRequest request,
                                                 VaadinResponse response)
                            throws IOException {
                        if ("/model.rvt.json.js".equals(request.getPathInfo()) && request.getParameter("id") != null) {
                            response.setContentType("text/javascript");
                            FileResource fileResource = BpmAttachmentService.getAttachmentFileResource(Long.valueOf(request.getParameter("id")));
                            fileResource.getStream().writeResponse(request, response);
                            return true; // We wrote a response
                        } else
                            return false; // No response was written
                    }
                });
        javaScript = JavaScript.getCurrent();
        javaScript.execute(String.format("init('%s')", componentId));
    }

    public void loadScene(Long attachmentId) {
        javaScript.execute(String.format("loadScene('model.rvt.json.js?id=%s&gc=%s')", attachmentId, System.currentTimeMillis()));
    }
    
    public void loadScene(Long attachmentId, List<PlanItemDTO> planItems) {
        JSONArray itemsJson = new JSONArray();
        itemsJson.addAll(planItems);
        javaScript.execute(String.format("loadScene('model.rvt.json.js?id=%s&gc=%s', %s)", attachmentId, System.currentTimeMillis(), itemsJson));
    }
    
    public static class PlanItemDTO {
        public final String name;
        public final String articleType;
        public final String level;
        public final String block;
        public final int quantity;
        public final int stockQuantity;
        public final int localStockQuantity;
        
        
        public PlanItemDTO(String name, String articleType, String level, String block, int quantity, int stockQuantity, int localStockQuantity) {
            this.name = name;
            this.articleType = articleType;
            this.level = level;
            this.block = block;
            this.quantity = quantity;
            this.stockQuantity = stockQuantity;
            this.localStockQuantity = localStockQuantity;
        }
    }
    
}
