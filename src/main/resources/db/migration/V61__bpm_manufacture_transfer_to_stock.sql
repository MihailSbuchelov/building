create sequence bpm_transfer_to_stock_id_sequence;
create table bpm_transfer_to_stock (
    id integer primary key,
    status varchar not null default 'new',
    day timestamp not null
);

create sequence bpm_transfer_to_stock_item_id_sequence;
create table bpm_transfer_to_stock_item (
    id integer primary key,
    bpm_transfer_to_stock_id integer not null references bpm_transfer_to_stock on delete restrict,
    bpm_manufacture_day_plan_line_building_object_item_id integer not null references bpm_manufacture_day_plan_line_building_object_item on delete restrict,
    
    quantity integer not null,
    approved_quantity integer not null
);