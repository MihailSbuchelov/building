package ru.everybit.bzkpd_bsk.components.manufacture.month_plan;

import ru.everybit.bzkpd_bsk.application.view.component.BpmModel;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlan;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItem;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSector;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLine;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanForBuildingPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanItem;

import java.util.*;

public class ManufactureMonthPlanModel implements BpmModel {
    // model
    private BpmManufactureMonthPlan manufactureMonthPlan;
    private List<BpmManufactureMonthPlanForBuildingPlan> manufactureMonthPlanForBuildingPlanList;
    private BpmBuildingObject buildingObject;
    private List<BpmBuildingPlanSector> buildingPlanSectorList;
    private List<BpmManufactureLine> manufactureLineList;
    private BpmArticleType articleType;
    private List<BpmBuildingPlanItem> buildingPlanItemList;
    private List<BpmArticle> articleListSortedById;
    private Map<BpmArticle, Map<BpmManufactureLine, List<BpmManufactureMonthPlanItem>>> manufactureMonthPlanItemMap;
    private BpmArticle article;

    public void setManufactureMonthPlanId(Long manufactureMonthPlanId) {
        manufactureMonthPlan = BpmDaoFactory.getManufactureMonthPlanDao().findOne(manufactureMonthPlanId);
    }

    @Override
    public void initModel() {
        manufactureMonthPlanForBuildingPlanList = BpmDaoFactory.getManufactureMonthPlanForBuildingPlanDao().
                findByManufactureMonthPlan(manufactureMonthPlan);
        manufactureLineList = BpmDaoFactory.getManufactureLineDao().findAll();
    }

    public List<BpmManufactureMonthPlanForBuildingPlan> getManufactureMonthPlanForBuildingPlanList() {
        return manufactureMonthPlanForBuildingPlanList;
    }

    public BpmManufactureMonthPlan getManufactureMonthPlan() {
        return manufactureMonthPlan;
    }

    public Date getManufacturePlanMonth() {
        return manufactureMonthPlan.getMonth();
    }

    public List<BpmBuildingObject> getBuildingObjectList() {
        List<BpmBuildingObject> buildingObjectList = new ArrayList<>();
        for (BpmManufactureMonthPlanForBuildingPlan manufactureBuildingPlan : manufactureMonthPlanForBuildingPlanList) {
            BpmBuildingObject buildingObject = manufactureBuildingPlan.getBuildingPlan().getBuildingObject();
            boolean found = false;
            for (BpmBuildingObject savedBuildingObject : buildingObjectList) {
                if (savedBuildingObject.getId().equals(buildingObject.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                buildingObjectList.add(buildingObject);
            }
        }
        return buildingObjectList;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
        buildingPlanSectorList = new ArrayList<>();
        for (BpmManufactureMonthPlanForBuildingPlan manufactureBuildingPlan : manufactureMonthPlanForBuildingPlanList) {
            BpmBuildingPlan buildingPlan = manufactureBuildingPlan.getBuildingPlan();
            if (buildingObject != null && !buildingPlan.getBuildingObject().getId().equals(buildingObject.getId())) {
                continue;
            }
            for (BpmBuildingPlanSector buildingPlanSector : buildingPlan.getSectors()) {
                buildingPlanSectorList.add(buildingPlanSector);
            }
        }
    }

    public List<BpmManufactureLine> getManufactureLineList() {
        return manufactureLineList;
    }

    public Collection<BpmArticleType> getArticleTypesSortedById() {
        Map<Long, BpmArticleType> articleTypeMap = new TreeMap<>();
        for (BpmBuildingPlanSector buildingPlanSector : buildingPlanSectorList) {
            for (BpmBuildingPlanItem buildingPlanItem : buildingPlanSector.getItems()) {
                BpmArticleType articleType = buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getArticleType();
                if (!articleTypeMap.containsKey(articleType.getId())) {
                    articleTypeMap.put(articleType.getId(), articleType);
                }
            }
        }
        return articleTypeMap.values();
    }

    public BpmArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(BpmArticleType articleType) {
        this.articleType = articleType;

        buildingPlanItemList = new ArrayList<>();
        for (BpmBuildingPlanSector buildingPlanSector : buildingPlanSectorList) {
            for (BpmBuildingPlanItem buildingPlanItem : buildingPlanSector.getItems()) {
                if (articleType.getId().equals(buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getArticleType().getId())) {
                    buildingPlanItemList.add(buildingPlanItem);
                }
            }
        }

        Map<Long, BpmArticle> articleMap = new TreeMap<>();
        for (BpmBuildingPlanItem buildingPlanItem : buildingPlanItemList) {
            BpmArticle article = buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle();
            if (article.getArticleType().getId().equals(articleType.getId())) {
                if (!articleMap.containsKey(article.getId())) {
                    articleMap.put(article.getId(), article);
                }
            }
        }
        articleListSortedById = new ArrayList<>(articleMap.values());

        manufactureMonthPlanItemMap = new HashMap<>();
        for (BpmArticle article : articleListSortedById) {
            if (!manufactureMonthPlanItemMap.containsKey(article)) {
                manufactureMonthPlanItemMap.put(article, new HashMap<BpmManufactureLine, List<BpmManufactureMonthPlanItem>>());
            }
            Map<BpmManufactureLine, List<BpmManufactureMonthPlanItem>> manufactureLineMap = manufactureMonthPlanItemMap.get(article);
            for (BpmManufactureLine manufactureLine : manufactureLineList) {
                if (!manufactureLineMap.containsKey(manufactureLine)) {
                    manufactureLineMap.put(manufactureLine, new ArrayList<BpmManufactureMonthPlanItem>());
                }
            }
        }
        List<BpmManufactureMonthPlanItem> manufacturePlanItemList;
        if (buildingObject == null) {
            manufacturePlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                    findByManufactureMonthPlanAndArticleArticleType(manufactureMonthPlan, articleType);
        } else {
            manufacturePlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                    findByManufactureMonthPlanAndArticleArticleTypeAndBuildingObject(manufactureMonthPlan, articleType, buildingObject);
        }
        for (BpmManufactureMonthPlanItem manufacturePlanItem : manufacturePlanItemList) {
            manufactureMonthPlanItemMap.get(manufacturePlanItem.getArticle()).get(manufacturePlanItem.getManufactureLine()).add(manufacturePlanItem);
        }
    }

    public List<BpmArticle> getArticlesSortedById() {
        return articleListSortedById;
    }

    public int getSumOfBuildingPlanItemQuantityGroupByArticle(BpmArticle article) {
        int quantity = 0;
        for (BpmBuildingPlanItem buildingPlanItem : buildingPlanItemList) {
            if (article.getId().equals(buildingPlanItem.getProduct().getBuildingObjectArticle().getArticle().getId())) {
                quantity += buildingPlanItem.getQuantity() - buildingPlanItem.getProductionReserveQuantity();
            }
        }
        return quantity;
    }

    public void setArticle(BpmArticle article) {
        this.article = article;
    }

    public BpmArticle getArticle() {
        return article;
    }

    public BpmManufactureMonthPlanItem getManufacturePlanItem(BpmManufactureLine manufactureLine, BpmArticle article) {
        List<BpmManufactureMonthPlanItem> manufacturePlanItemList = manufactureMonthPlanItemMap.get(article).get(manufactureLine);
        if (manufacturePlanItemList.isEmpty()) {
            BpmManufactureMonthPlanItem manufacturePlanItem = new BpmManufactureMonthPlanItem();
            manufacturePlanItem.setManufactureMonthPlan(manufactureMonthPlan);
            manufacturePlanItem.setBuildingObject(buildingObject);
            manufacturePlanItem.setManufactureLine(manufactureLine);
            manufacturePlanItem.setArticle(article);
            manufacturePlanItem.setQuantity(0);
            return manufacturePlanItem;
        } else if (manufacturePlanItemList.size() == 1) {
            return manufacturePlanItemList.get(0);
        }
        throw new RuntimeException("only one element expected");
    }

    public void saveManufacturePlanItem(BpmManufactureMonthPlanItem manufacturePlanItem) {
        boolean newItem = manufacturePlanItem.getId() == null;
        manufacturePlanItem = BpmDaoFactory.getManufactureMonthPlanItemDao().save(manufacturePlanItem);
        if (newItem) {
            manufactureMonthPlanItemMap.get(manufacturePlanItem.getArticle()).get(manufacturePlanItem.getManufactureLine()).add(manufacturePlanItem);
        }
    }

    public void deleteManufacturePlanItem(BpmManufactureMonthPlanItem manufacturePlanItem) {
        if (manufacturePlanItem.getId() != null) {
            List<BpmManufactureMonthPlanItem> manufacturePlanItemList = manufactureMonthPlanItemMap.get(manufacturePlanItem.getArticle()).get(manufacturePlanItem.getManufactureLine());
            for (BpmManufactureMonthPlanItem savedManufacturePlanItem : new ArrayList<>(manufacturePlanItemList)) {
                if (savedManufacturePlanItem.getId().equals(manufacturePlanItem.getId())) {
                    manufacturePlanItemList.remove(savedManufacturePlanItem);
                }
            }
            BpmDaoFactory.getManufactureMonthPlanItemDao().delete(manufacturePlanItem);
        }
    }

    public int getSumOfManufacturePlanItems(BpmArticle article, BpmManufactureLine manufactureLine) {
        List<BpmManufactureMonthPlanItem> manufacturePlanItemList = manufactureMonthPlanItemMap.get(article).get(manufactureLine);
        int sum = 0;
        for (BpmManufactureMonthPlanItem manufacturePlanItem : manufacturePlanItemList) {
            sum += manufacturePlanItem.getQuantity();
        }
        return sum;
    }
}
