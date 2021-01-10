-- Добавляем ссылку на объект в план строительства
alter table BPM_BUILDING_PLAN add column bpm_building_object_id integer references bpm_building_object;
create index bpm_building_plan_bpm_object_idx on BPM_BUILDING_PLAN(bpm_building_object_id);

-- Переименовываем section в sector
ALTER SEQUENCE BPM_BUILDING_PLAN_SECTION_ID_SEQUENCE RENAME TO BPM_BUILDING_PLAN_SECTOR_ID_SEQUENCE;
ALTER TABLE bpm_building_plan_section RENAME TO bpm_building_plan_sector;

-- Создаем таблизу для сохранения изделий в плане строительства
create sequence bpm_building_plan_item_id_sequence;
create table bpm_building_plan_item (
    id integer not null primary key,
    bpm_product_id integer not null,
    quantity integer not null default 0,
    bpm_building_plan_sector_id integer not null references bpm_building_plan_sector
);

create index bpm_building_plan_item_bpm_building_plan_sector_idx on bpm_building_plan_item(bpm_building_plan_sector_id);
create index bpm_building_plan_item_bpm_product_idx on bpm_building_plan_item(bpm_product_id);