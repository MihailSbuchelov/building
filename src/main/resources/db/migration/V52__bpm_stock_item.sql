create sequence bpm_stock_item_id_sequence;
create table bpm_stock_item (
    id integer primary key,
    bpm_building_plan_item_id integer not null references bpm_building_plan_item on delete restrict,
    quantity integer not null
);