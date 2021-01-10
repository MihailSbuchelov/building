-- добавить в таблицу bpm_block поле priority

alter table bpm_block
    add column priority integer not null;