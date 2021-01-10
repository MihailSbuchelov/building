package ru.everybit.bzkpd_bsk.application.service.security;

public enum BpmRole {
    ADMIN("admin", "Администратор"),
    BUILDING_INITIATOR("BuildingInitiator", "Инициатор строительства"),
    DESIGNER("Designer", "Дизайнер"),
    PTO_BSK_EMPLOYEE("PtoBskEmployee", "Сотрудник ПТО БСК"),
    PTO_BZKPD_EMPLOYEE("PtoBzkpdEmployee", "Сотрудник ПТО БЗКПД"),
    PTO_BZKPD_MANAGER("PtoBzkpdManager", "Начальник ПТО БЗКПД"),
    FOREMAN("Foreman", "Прораб"),
    MANUFACTURE_EMPLOYEE("ManufactureEmployee", "Мастер цеха"),
    WAREHOUSE_MANAGER("WarehouseManager", "Начальник склада"),
    CONTROLLING_UNIT("ControllingUnit", "Контролирующее подразделение");

    private String id;
    private String name;

    BpmRole(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
