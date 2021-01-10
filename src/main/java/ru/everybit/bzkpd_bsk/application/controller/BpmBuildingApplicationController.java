package ru.everybit.bzkpd_bsk.application.controller;

import com.vaadin.server.FontAwesome;
import ru.everybit.bzkpd_bsk.application.controller.process.*;
import ru.everybit.bzkpd_bsk.application.controller.report.*;
import ru.everybit.bzkpd_bsk.application.controller.task.BpmMyTaskController;
import ru.everybit.bzkpd_bsk.application.controller.task.BpmSearchTaskController;
import ru.everybit.bzkpd_bsk.application.service.security.BpmRole;
import ru.everybit.bzkpd_bsk.application.service.security.BpmSecurityService;
import ru.everybit.bzkpd_bsk.application.view.component.BpmButtonGroupComponent;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmNavigation;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmPortal;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;
import ru.everybit.bzkpd_bsk.references.article.ArticleReferenceController;
import ru.everybit.bzkpd_bsk.references.users.UsersReferenceController;

import java.io.Serializable;

public class BpmBuildingApplicationController implements Serializable {
    private BpmPortal portal;
    private BpmNavigation navigation;
    private BpmWorkingArea workingArea;

    public BpmBuildingApplicationController(BpmPortal portal) {
        this.portal = portal;
        navigation = new BpmNavigation();
        workingArea = new BpmWorkingArea();

        // call setters
        portal.setNavigationContent(navigation.getUI());
        portal.setWorkingAreaContent(workingArea.getUi());
        navigation.setButtonGroupListener(new ButtonGroupComponentListenerImplementation());
    }

    public void init() {
        portal.buildUI();

        // navigation
        initTaskNavigation();
        initReferenceNavigation();
        initReportNavigation();
        initProcessNavigation();
        navigation.navigateToTheFirstForm();
    }

    private void initTaskNavigation() {
        navigation.addSeparator("Задачи");
        navigation.addNavigationElement(BpmMyTaskController.class, "Мои задачи", FontAwesome.TASKS);
        navigation.addNavigationElement(BpmSearchTaskController.class, "Поиск задач", FontAwesome.SEARCH);
    }

    private void initReferenceNavigation() {
        boolean articleReference = BpmSecurityService.checkRoles(BpmRole.PTO_BSK_EMPLOYEE, BpmRole.PTO_BZKPD_MANAGER);
        boolean usersReference = BpmSecurityService.checkRoles(BpmRole.ADMIN);
        if (articleReference || usersReference) {
            navigation.addSeparator("Справочники");
        }
        if (articleReference) {
            navigation.addNavigationElement(ArticleReferenceController.class, "Номенклатура изделий", FontAwesome.LANGUAGE);
        }
        if (usersReference) {
            navigation.addNavigationElement(UsersReferenceController.class, "Пользователи", FontAwesome.USERS);
        }
    }

    private void initReportNavigation() {
        navigation.addSeparator("Отчеты");
        navigation.addNavigationElement(PickingListReportController.class, "Комплектовочная ведомость", FontAwesome.RENREN);
        navigation.addNavigationElement(BpmThreeDReportController.class, "3D-отчет", FontAwesome.BUILDING);
        navigation.addNavigationElement(ManufactureMonthPlanReportController.class, "План производства", FontAwesome.RENREN);
        navigation.addNavigationElement(BuildingPlanReportController.class, "Строительство объекта", FontAwesome.BOOKMARK);
        navigation.addNavigationElement(LogisticsReportController.class, "Отчет по складам", FontAwesome.BOOKMARK);

    }

    private void initProcessNavigation() {
        boolean buildingInitiator = BpmSecurityService.checkRoles(BpmRole.BUILDING_INITIATOR);
        boolean ptoBzkpdManager = BpmSecurityService.checkRoles(BpmRole.PTO_BZKPD_MANAGER);
        boolean warehouseManager = BpmSecurityService.checkRoles(BpmRole.WAREHOUSE_MANAGER);
        boolean manufactureEmployee = BpmSecurityService.checkRoles(BpmRole.MANUFACTURE_EMPLOYEE); 
        boolean foreman = BpmSecurityService.checkRoles(BpmRole.FOREMAN);
        if (buildingInitiator || ptoBzkpdManager || warehouseManager || foreman) {
            navigation.addSeparator("Запустить процесс");
        }
        if (buildingInitiator) {
            navigation.addNavigationElement(BpmBuildingMainProcessController.class, "Начать строительство", FontAwesome.PLAY);
        }
        if (ptoBzkpdManager) {
            navigation.addNavigationElement(BpmManufactureMonthPlanProcessController.class, "План производства", FontAwesome.PLAY);
        }
        if(manufactureEmployee){
            navigation.addNavigationElement(BpmTransferToStockProcessController.class, "Передать на склад", FontAwesome.PLAY);
        }
        if (warehouseManager) {
            navigation.addNavigationElement(BpmTransferToLocalStockProcessController.class, "Транспортировка на объект", FontAwesome.PLAY);
            navigation.addNavigationElement(BpmWriteOffFromStockProcessController.class, "Списать со склада", FontAwesome.PLAY);
        }
        if (foreman) {
            navigation.addNavigationElement(BpmWriteOffFromLocalStockProcessController.class, "Списать со стройки", FontAwesome.PLAY);
        }
    }

    private final class ButtonGroupComponentListenerImplementation implements BpmButtonGroupComponent.BpmButtonGroupComponentListener {
        @Override
        public void buttonPressed(String buttonId) {
            if (BpmMyTaskController.class.getSimpleName().equals(buttonId)) {
                BpmMyTaskController.init(workingArea);
            } else if (BpmSearchTaskController.class.getSimpleName().equals(buttonId)) {
                BpmSearchTaskController.init(workingArea);
            } else if (ArticleReferenceController.class.getSimpleName().equals(buttonId)) {
                ArticleReferenceController.init(workingArea);
            } else if (UsersReferenceController.class.getSimpleName().equals(buttonId)) {
                UsersReferenceController.init(workingArea);
            } else if (PickingListReportController.class.getSimpleName().equals(buttonId)) {
                PickingListReportController.init(workingArea);
            } else if (BpmThreeDReportController.class.getSimpleName().equals(buttonId)) {
                BpmThreeDReportController.init(workingArea);
            } else if (BuildingPlanReportController.class.getSimpleName().equals(buttonId)) {
                BuildingPlanReportController.init(workingArea);
            } else if (ManufactureMonthPlanReportController.class.getSimpleName().equals(buttonId)) {
                ManufactureMonthPlanReportController.init(workingArea);
            } else if (BpmBuildingMainProcessController.class.getSimpleName().equals(buttonId)) {
                BpmBuildingMainProcessController.init(workingArea);
            } else if (BpmManufactureMonthPlanProcessController.class.getSimpleName().equals(buttonId)) {
                BpmManufactureMonthPlanProcessController.init(workingArea);
            } else if (BpmTransferToLocalStockProcessController.class.getSimpleName().equals(buttonId)) {
                BpmTransferToLocalStockProcessController.init(workingArea);
            } else if (BpmWriteOffFromStockProcessController.class.getSimpleName().equals(buttonId)) {
                BpmWriteOffFromStockProcessController.init(workingArea);
            } else if (BpmWriteOffFromLocalStockProcessController.class.getSimpleName().equals(buttonId)) {
                BpmWriteOffFromLocalStockProcessController.init(workingArea);
            } else if (LogisticsReportController.class.getSimpleName().equals(buttonId)) {
                LogisticsReportController.init(workingArea);
            }else if (BpmTransferToStockProcessController.class.getSimpleName().equals(buttonId)) {
                BpmTransferToStockProcessController.init(workingArea);
            }
        }
    }
}