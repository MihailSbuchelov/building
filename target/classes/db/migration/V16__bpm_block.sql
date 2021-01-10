-- добавить в таблицу bpm_block foreign key на таблицу bpm_building_object

delete from bpm_product;
delete from bpm_sector;
delete from bpm_block;

alter table bpm_block
    add column bpm_building_object_id integer not null references bpm_building_object;