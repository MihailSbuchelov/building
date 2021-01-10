package ru.everybit.bzkpd_bsk.model;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleKjiDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleTypeDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBlockDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObjectArticleDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObjectDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmFloorDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmProductDao;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmSectorDao;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanDao;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanItemDao;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReportDao;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanReportSectorDao;
import ru.everybit.bzkpd_bsk.model.building.building_plan.BpmBuildingPlanSectorDao;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShippingDao;
import ru.everybit.bzkpd_bsk.model.logistics.BpmShippingItemDao;
import ru.everybit.bzkpd_bsk.model.logistics.BpmStockItemDao;
import ru.everybit.bzkpd_bsk.model.manufacture.BpmManufactureLineDao;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanDao;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineBuildingObjectItemDao;
import ru.everybit.bzkpd_bsk.model.manufacture.day_plan.BpmManufactureDayPlanLineDao;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanDao;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanForBuildingPlanDao;
import ru.everybit.bzkpd_bsk.model.manufacture.month_plan.BpmManufactureMonthPlanItemDao;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockDao;
import ru.everybit.bzkpd_bsk.model.manufacture.transfer_to_stock.BpmTransferToStockItemDao;

@Component
public class BpmDaoFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BpmDaoFactory.applicationContext = applicationContext;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return applicationContext.getBean(EntityManagerFactory.class);
    }

    public static BpmBuildingObjectDao getBuildingObjectDao() {
        return applicationContext.getBean(BpmBuildingObjectDao.class);
    }

    public static BpmManufactureLineDao getManufactureLineDao() {
        return applicationContext.getBean(BpmManufactureLineDao.class);
    }

    public static BpmFloorDao getFloorDao() {
        return applicationContext.getBean(BpmFloorDao.class);
    }

    public static BpmBlockDao getBlockDao() {
        return applicationContext.getBean(BpmBlockDao.class);
    }

    public static BpmSectorDao getSectorDao() {
        return applicationContext.getBean(BpmSectorDao.class);
    }

    public static BpmArticleTypeDao getArticleTypeDao() {
        return applicationContext.getBean(BpmArticleTypeDao.class);
    }

    public static BpmArticleDao getArticleDao() {
        return applicationContext.getBean(BpmArticleDao.class);
    }

    public static BpmProductDao getProductDao() {
        return applicationContext.getBean(BpmProductDao.class);
    }

    public static BpmBuildingObjectArticleDao getBuildingObjectArticleDao() {
        return applicationContext.getBean(BpmBuildingObjectArticleDao.class);
    }

    public static BpmBuildingPlanDao getBuildingPlanDao() {
        return applicationContext.getBean(BpmBuildingPlanDao.class);
    }

    public static BpmBuildingPlanSectorDao getBuildingPlanSectorDao() {
        return applicationContext.getBean(BpmBuildingPlanSectorDao.class);
    }

    public static BpmManufactureMonthPlanItemDao getManufactureMonthPlanItemDao() {
        return applicationContext.getBean(BpmManufactureMonthPlanItemDao.class);
    }

    public static BpmManufactureMonthPlanDao getManufactureMonthPlanDao() {
        return applicationContext.getBean(BpmManufactureMonthPlanDao.class);
    }

    public static BpmManufactureMonthPlanForBuildingPlanDao getManufactureMonthPlanForBuildingPlanDao() {
        return applicationContext.getBean(BpmManufactureMonthPlanForBuildingPlanDao.class);
    }

    public static BpmBuildingPlanItemDao getBuildingPlanItemDao() {
        return applicationContext.getBean(BpmBuildingPlanItemDao.class);
    }

    public static BpmArticleKjiDao getArticleKjiDao() {
        return applicationContext.getBean(BpmArticleKjiDao.class);
    }

    public static BpmBuildingPlanReportDao getBuildingPlanReportDao() {
        return applicationContext.getBean(BpmBuildingPlanReportDao.class);
    }

    public static BpmBuildingPlanReportSectorDao getBuildingPlanReportSectorDao() {
        return applicationContext.getBean(BpmBuildingPlanReportSectorDao.class);
    }

    public static BpmManufactureDayPlanDao getManufactureDayPlanDao() {
        return applicationContext.getBean(BpmManufactureDayPlanDao.class);
    }

    public static BpmManufactureDayPlanLineDao getManufactureDayPlanLineDao() {
        return applicationContext.getBean(BpmManufactureDayPlanLineDao.class);
    }

    public static BpmManufactureDayPlanLineBuildingObjectItemDao getManufactureDayPlanLineBuildingObjectItemDao() {
        return applicationContext.getBean(BpmManufactureDayPlanLineBuildingObjectItemDao.class);
    }

    public static BpmStockItemDao getStockItemDao() {
        return applicationContext.getBean(BpmStockItemDao.class);
    }

    public static BpmShippingDao getShippingDao() {
        return applicationContext.getBean(BpmShippingDao.class);
    }

    public static BpmShippingItemDao getShippingItemDao() {
        return applicationContext.getBean(BpmShippingItemDao.class);
    }

    public static BpmTransferToStockDao getTransferToStockDao() {
        return applicationContext.getBean(BpmTransferToStockDao.class);
    }

    public static BpmTransferToStockItemDao getTransferToStockItemDao() {
        return applicationContext.getBean(BpmTransferToStockItemDao.class);
    }
    
}
