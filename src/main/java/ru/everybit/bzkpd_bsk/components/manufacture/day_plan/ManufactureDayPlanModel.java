package ru.everybit.bzkpd_bsk.components.manufacture.day_plan;

import ru.everybit.bzkpd_bsk.application.view.component.BpmModel;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlan;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLine;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItem;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanItem;

import java.util.*;

public class ManufactureDayPlanModel implements BpmModel {
    private BpmManufactureDayPlan manufactureDayPlan;
    private List<BpmManufactureDayPlanLine> manufactureDayPlanLineList;
    private BpmManufactureDayPlanLine manufactureDayPlanLine;
    private List<BpmManufactureMonthPlanItem> manufactureMonthPlanItemList;
    private BpmArticleType articleType;
    private List<BpmArticle> articleListSortedById;
    private BpmBuildingObject buildingObject;
    private BpmArticle article;

    public void setManufactureDayPlanId(Long manufactureDayPlanId) {
        manufactureDayPlan = BpmDaoFactory.getManufactureDayPlanDao().findOne(manufactureDayPlanId);
    }

    @Override
    public void initModel() {
        manufactureDayPlanLineList = BpmDaoFactory.getManufactureDayPlanLineDao().
                findByManufactureDayPlanOrderByManufactureLineNameAsc(manufactureDayPlan);
    }

    public Date getManufacturePlanDay() {
        return manufactureDayPlan.getDay();
    }

    public List<BpmManufactureDayPlanLine> getManufactureDayPlanLineList() {
        return manufactureDayPlanLineList;
    }

    public void setManufactureDayPlanLine(BpmManufactureDayPlanLine manufactureDayPlanLine) {
        this.manufactureDayPlanLine = manufactureDayPlanLine;
    }

    public List<BpmBuildingObject> getBuildingObjectList() {
        List<BpmBuildingObject> buildingObjectList = new ArrayList<>();
        List<BpmManufactureMonthPlanItem> manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                findByManufactureMonthPlan(manufactureDayPlan.getManufactureMonthPlan());
        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : manufactureMonthPlanItemList) {
//            if (!manufactureMonthPlanItem.getManufactureLine().getId().equals(manufactureDayPlanLine.getManufactureLine().getId())) {
//                continue;
//            }
            BpmBuildingObject buildingObject = manufactureMonthPlanItem.getBuildingObject();
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
        if (buildingObject == null) {
//            manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
//                    findByManufactureMonthPlanAndManufactureLine(manufactureDayPlan.getManufactureMonthPlan(), manufactureDayPlanLine.getManufactureLine());
            manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                    findByManufactureMonthPlan(manufactureDayPlan.getManufactureMonthPlan());

        } else {
//            manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
//                    findByManufactureMonthPlanAndManufactureLineAndBuildingObject(manufactureDayPlan.getManufactureMonthPlan(), manufactureDayPlanLine.getManufactureLine(), buildingObject);
            manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                    findByManufactureMonthPlanAndBuildingObject(manufactureDayPlan.getManufactureMonthPlan(), buildingObject);
        }
    }

    public List<BpmArticleType> getArticleTypesSortedById() {
        Map<Long, BpmArticleType> articleMap = new TreeMap<>();
        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : manufactureMonthPlanItemList) {
            BpmArticleType articleType = manufactureMonthPlanItem.getArticle().getArticleType();
            if (!articleMap.containsKey(articleType.getId())) {
                articleMap.put(articleType.getId(), articleType);
            }
        }
        return new ArrayList<>(articleMap.values());
    }

    public void setArticleType(BpmArticleType articleType) {
        this.articleType = articleType;

        Map<Long, BpmArticle> articleMap = new TreeMap<>();
        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : manufactureMonthPlanItemList) {
            if (articleType.getId().equals(manufactureMonthPlanItem.getArticle().getArticleType().getId())) {
                articleMap.put(manufactureMonthPlanItem.getArticle().getId(), manufactureMonthPlanItem.getArticle());
            }
        }
        articleListSortedById = new ArrayList<>(articleMap.values());
    }

    public BpmArticleType getArticleType() {
        return articleType;
    }

    public List<BpmArticle> getArticlesSortedById() {
        return articleListSortedById;
    }

    public int getSumOfManufactureMonthPlanItemQuantityGroupByArticle(BpmArticle article) {
        int total = 0;
        List<BpmManufactureDayPlanLineBuildingObjectItem> buildingObjectItemList;
        if (buildingObject == null) {
            buildingObjectItemList = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                    findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndArticle(manufactureDayPlan.getManufactureMonthPlan(), article);
        } else {
            buildingObjectItemList = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                    findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndBuildingObjectAndArticle(manufactureDayPlan.getManufactureMonthPlan(), buildingObject, article);
        }
        for (BpmManufactureDayPlanLineBuildingObjectItem buildingObjectItem : buildingObjectItemList) {
            total += buildingObjectItem.getQuantity();
        }
        return total;
    }

    public int getSumOfManufactureMonthPlanItemPlanQuantityGroupByArticle(BpmArticle article) {
        int total = 0;
        List<BpmManufactureMonthPlanItem> manufactureMonthPlanItemList;
        if (buildingObject == null) {
            manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                    findByManufactureMonthPlanAndArticle(manufactureDayPlan.getManufactureMonthPlan(), article);
        } else {
            manufactureMonthPlanItemList = BpmDaoFactory.getManufactureMonthPlanItemDao().
                    findByManufactureMonthPlanAndBuildingObjectAndArticle(manufactureDayPlan.getManufactureMonthPlan(), buildingObject, article);
        }
        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : manufactureMonthPlanItemList) {
            total += manufactureMonthPlanItem.getQuantity();
        }
        return total;
    }


    public int getSumOfManufactureMonthPlanItemQuantityGroupByArticleAndSelectedManufactureLine(BpmArticle article) {
        int total = 0;
        List<BpmManufactureDayPlanLineBuildingObjectItem> buildingObjectItemList;
        if (buildingObject == null) {
            buildingObjectItemList = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                    findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndArticle(manufactureDayPlan.getManufactureMonthPlan(), article);
        } else {
            buildingObjectItemList = BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                    findByManufactureDayPlanLineManufactureDayPlanManufactureMonthPlanAndBuildingObjectAndArticle(manufactureDayPlan.getManufactureMonthPlan(), buildingObject, article);
        }
        for (BpmManufactureDayPlanLineBuildingObjectItem buildingObjectItem : buildingObjectItemList) {
            if (manufactureDayPlanLine.getManufactureLine().getId().equals(buildingObjectItem.getManufactureDayPlanLine().getManufactureLine().getId())) {
                total += buildingObjectItem.getQuantity();
            }
        }
        return total;
    }

    public int getSumOfManufactureMonthPlanItemPlanQuantityGroupByArticleAndSelectedManufactureLine(BpmArticle article) {
        int total = 0;
        for (BpmManufactureMonthPlanItem manufactureMonthPlanItem : manufactureMonthPlanItemList) {
            if (article.getId().equals(manufactureMonthPlanItem.getArticle().getId())
                    && manufactureMonthPlanItem.getManufactureLine().getId().equals(manufactureDayPlanLine.getManufactureLine().getId())) {
                total += manufactureMonthPlanItem.getQuantity();
            }
        }
        return total;
    }

    public void setArticle(BpmArticle article) {
        this.article = article;
    }

    public BpmArticle getArticle() {
        return article;
    }

    public int getManufactureDayPlanItemQuantity(BpmArticle article) {
        if (buildingObject == null) {
            int quantity = 0;
            for (BpmManufactureDayPlanLineBuildingObjectItem manufactureDayPlanItem : BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                    findByManufactureDayPlanLineAndArticle(manufactureDayPlanLine, article)) {
                quantity += manufactureDayPlanItem.getPlanQuantity();
            }
            return quantity;
        } else {
            BpmManufactureDayPlanLineBuildingObjectItem manufactureDayPlanItem = getManufactureDayPlanItem(article);
            return manufactureDayPlanItem == null ? 0 : manufactureDayPlanItem.getPlanQuantity();
        }
    }

    public void deleteManufactureDayPlanItem(BpmArticle article) {
        BpmManufactureDayPlanLineBuildingObjectItem manufactureDayPlanItem = getManufactureDayPlanItem(article);
        if (manufactureDayPlanItem != null) {
            BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().delete(manufactureDayPlanItem);
        }
    }

    public void updateManufactureDayPlanItemPlanQuantity(BpmArticle article, int quantity) {
        BpmManufactureDayPlanLineBuildingObjectItem manufactureDayPlanItem = getManufactureDayPlanItem(article);
        if (manufactureDayPlanItem == null) {
            manufactureDayPlanItem = new BpmManufactureDayPlanLineBuildingObjectItem();
            manufactureDayPlanItem.setManufactureDayPlanLine(manufactureDayPlanLine);
            manufactureDayPlanItem.setBuildingObject(buildingObject);
            manufactureDayPlanItem.setArticle(article);
            manufactureDayPlanItem.setPlanQuantity(0);
        }
        manufactureDayPlanItem.setPlanQuantity(quantity);
        BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().save(manufactureDayPlanItem);
    }

    private BpmManufactureDayPlanLineBuildingObjectItem getManufactureDayPlanItem(BpmArticle article) {
        return BpmDaoFactory.getManufactureDayPlanLineBuildingObjectItemDao().
                findByManufactureDayPlanLineAndBuildingObjectAndArticle(manufactureDayPlanLine, buildingObject, article);
    }

    public String getManufactureDayPlanLineManagerComment() {
        return manufactureDayPlanLine.getManagerComment();
    }

    public void updateManufactureDayPlanLineManagerComment(String comment) {
        manufactureDayPlanLine.setManagerComment(comment);
        BpmDaoFactory.getManufactureDayPlanLineDao().save(manufactureDayPlanLine);
    }
}
