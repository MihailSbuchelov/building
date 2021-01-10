package ru.everybit.bzkpd_bsk.processes.building.main.create_picking_list;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;
import ru.everybit.bzkpd_bsk.application.view.component.BpmTable;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardForm;
import ru.everybit.bzkpd_bsk.application.view.form.BpmWizardFormStep;
import ru.everybit.bzkpd_bsk.model.building.BpmBuildingProcessModel;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBlock;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBuildingObject;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmFloor;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmSector;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class CreatePickingListFormStep2 extends BpmWizardFormStep {
    private List<BpmBlock> blockList;
    private List<BpmFloor> floorList;
    // view
    private BpmTable blockTable;

    public CreatePickingListFormStep2(BpmWizardForm wizardForm) {
        super(CreatePickingListFormStep2.class.getSimpleName(), wizardForm);
        blockTable = new BpmTable();
    }

    @Override
    public void initModel() {
        Integer floorCount = (Integer) getForm().getTaskVariable(CreatePickingListForm.VARIABLE_FLOOR_COUNT);
        floorList = new ArrayList<>(floorCount);
        floorList.add(createFloor(0, "Ниже 0", true));
        for (int i = 1; i <= floorCount; i++) {
            floorList.add(createFloor(i, "Этаж " + i, false));
        }
        floorList.add(createFloor(floorCount + 1, "Чердак", false));

        Integer blockCount = (Integer) getForm().getTaskVariable(CreatePickingListForm.VARIABLE_BLOCK_COUNT);
        blockList = new ArrayList<>(blockCount);
        for (int i = 0; i < blockCount; i++) {
            blockList.add(new BpmBlock(i));
        }
        blockTable.setContainerDataSource(new BeanItemContainer<>(BpmBlock.class, blockList));
    }

    private BpmFloor createFloor(int priority, String name, boolean lowerFloor) {
        BpmFloor floor = new BpmFloor();
        floor.setPriority(priority);
        floor.setName(name);
        floor.setLowerFloor(lowerFloor);
        return floor;
    }

    @Override
    public void initView() {
        blockTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        blockTable.setEditable(true);
        blockTable.setPageLength(5);
        blockTable.setVisibleColumns("name");
        blockTable.setColumnHeaders("Наименование секций");
        wizardFormStepLayout.addComponent(blockTable);
    }

    @Override
    public void initControl() {
    }

    @Override
    public void eventNextButtonClick() {
        Long buildingObjectId = (Long) getForm().getProcessVariable(BpmBuildingProcessModel.BUILDING_OBJECT_ID);
        BpmBuildingObject buildingObject = BpmDaoFactory.getBuildingObjectDao().findOne(buildingObjectId);

        for (BpmFloor floor : floorList) {
            floor.setBuildingObject(buildingObject);
            floor = BpmDaoFactory.getFloorDao().save(floor);

            for (BpmBlock block : blockList) {
                block.setBuildingObject(buildingObject);
                block = BpmDaoFactory.getBlockDao().save(block);

                BpmSector sector = new BpmSector();
                sector.setFloor(floor);
                sector.setBlock(block);
                BpmDaoFactory.getSectorDao().save(sector);
            }
        }
    }
}
