-- добавить в таблицу bpm_floor поле priority

delete from bpm_product;
delete from bpm_sector;
delete from bpm_block;
delete from bpm_floor;

alter table bpm_floor
    add column priority integer not null;