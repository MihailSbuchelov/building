package ru.everybit.bzkpd_bsk.components.building.main.picking_list;

import ru.everybit.bzkpd_bsk.application.view.component.BpmModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.*;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class PickingListModel implements BpmModel {
    // model
    private BpmBuildingObject buildingObject;
    private List<BpmArticleType> articleTypeList;
    private BpmArticleType articleType;
    private List<BpmArticle> articleList;
    private List<BpmBuildingObjectArticle> buildingObjectArticleList;
    private List<BpmBlock> blockList;
    private List<BpmSector> sectorList;

    public BpmBuildingObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(BpmBuildingObject buildingObject) {
        this.buildingObject = buildingObject;
    }

    @Override
    public void initModel() {
        sectorList = BpmDaoFactory.getSectorDao().findByBuildingObjectIdOrderByFloorPriorityAndBlockPriority(buildingObject.getId());
        initBlockList();
        articleTypeList = BpmDaoFactory.getArticleTypeDao().findByDeletedOrderByIdAsc(false);
        for (BpmArticleType articleType : articleTypeList) {
            Integer totalQuantity = BpmDaoFactory.getProductDao().totalQuantity(buildingObject.getId(), articleType.getId());
            articleType.setTotalProductQuantity(totalQuantity);
        }
    }

    private void initBlockList() {
        blockList = new ArrayList<>();
        BpmFloor floor = null;
        for (BpmSector sector : sectorList) {
            if (floor == null) {
                floor = sector.getFloor();
            }
            if (sector.getFloor().equals(floor)) {
                blockList.add(sector.getBlock());
            } else {
                break;
            }
        }
    }

    public List<BpmArticleType> getArticleTypeList() {
        return articleTypeList;
    }

    public BpmArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(BpmArticleType articleType) {
        this.articleType = articleType;
        articleList = BpmDaoFactory.getArticleDao().findByArticleTypeAndDeletedOrderByIdAsc(this.articleType, false);
        buildingObjectArticleList = BpmDaoFactory.getBuildingObjectArticleDao().
                findByBuildingObjectAndArticleArticleTypeAndArticleDeletedOrderByIdAsc(buildingObject, articleType, false);
    }

    public List<BpmArticle> getArticleList() {
        return articleList;
    }

    public List<BpmArticle> getDistinctArticleList() {
        List<BpmArticle> distinctArticleList = new ArrayList<>();
        for (BpmArticle article : articleList) {
            boolean found = false;
            for (BpmBuildingObjectArticle buildingObjectArticle : buildingObjectArticleList) {
                if (article.getId().equals(buildingObjectArticle.getArticle().getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                distinctArticleList.add(article);
            }
        }
        return distinctArticleList;
    }

    public List<BpmBuildingObjectArticle> getBuildingObjectArticleList() {
        return buildingObjectArticleList;
    }

    public List<BpmBlock> getBlockList() {
        return blockList;
    }

    public List<BpmSector> getSectorList() {
        return sectorList;
    }

    public BpmBuildingObjectArticle createBuildingObjectArticle(BpmArticle article) {
        BpmBuildingObjectArticle buildingObjectArticle = new BpmBuildingObjectArticle();
        buildingObjectArticle.setArticle(article);
        buildingObjectArticle.setBuildingObject(buildingObject);
        buildingObjectArticle = BpmDaoFactory.getBuildingObjectArticleDao().save(buildingObjectArticle);
        buildingObjectArticleList.add(buildingObjectArticle);
        return buildingObjectArticle;
    }

    public void deleteBuildingObjectArticle(BpmBuildingObjectArticle buildingObjectArticle) {
        buildingObjectArticleList.remove(buildingObjectArticle);
        for (BpmProduct product : getProductList(buildingObjectArticle)) {
            updateProduct(buildingObjectArticle, product.getSector(), null);
        }
        BpmDaoFactory.getBuildingObjectArticleDao().delete(buildingObjectArticle);
    }

    public List<BpmProduct> getProductList(BpmBuildingObjectArticle buildingObjectArticle) {
        return BpmDaoFactory.getProductDao().
                findByBuildingObjectArticle(buildingObjectArticle);
    }

    public void updateProduct(BpmBuildingObjectArticle buildingObjectArticle, BpmSector sector, Integer quantity) {
        BpmProduct product = BpmDaoFactory.getProductDao().findByBuildingObjectArticleAndSector(buildingObjectArticle, sector);
        if (product == null) {
            if (quantity == null) {
                return;
            }
            product = new BpmProduct();
            product.setBuildingObjectArticle(buildingObjectArticle);
            product.setSector(sector);
            product.setQuantity(0);
        }
        int oldQuantity = product.getQuantity();
        if (quantity != null) {
            product.setQuantity(quantity);
            BpmDaoFactory.getProductDao().save(product);
        } else {
            BpmDaoFactory.getProductDao().delete(product);
            quantity = 0;
        }
        for (BpmArticleType articleType : articleTypeList) {
            if (articleType.getId().equals(buildingObjectArticle.getArticle().getArticleType().getId())) {
                int totalProductQuantity = articleType.getTotalProductQuantity() == null ? 0 : articleType.getTotalProductQuantity();
                articleType.setTotalProductQuantity(totalProductQuantity - oldQuantity + quantity);
            }
        }
    }

    public List<BpmBuildingObjectArticle> getUsedDeletedBuildingObjectArticleList() {
        return BpmDaoFactory.getBuildingObjectArticleDao().
                findByBuildingObjectAndArticleDeletedOrderByIdAsc(buildingObject, true);
    }
}
