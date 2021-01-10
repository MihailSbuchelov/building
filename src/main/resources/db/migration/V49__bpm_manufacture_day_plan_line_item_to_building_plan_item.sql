drop sequence bpm_manufacture_building_item_link_id_sequence;
drop table bpm_manufacture_building_item_link;

create sequence bpm_manufacture_day_plan_line_item_to_building_plan_item_id_sequence;
create table bpm_manufacture_day_plan_line_item_to_building_plan_item (
    id integer primary key,
    bpm_manufacture_day_plan_line_item_id integer not null references bpm_manufacture_day_plan_line_item on delete restrict,
    bpm_building_plan_item_id integer not null references bpm_building_plan_item on delete restrict,
    quantity integer not null
);

alter table bpm_manufacture_day_plan_line_building_object_item
    drop column quantity;

alter table bpm_manufacture_day_plan_line_building_object_item
    rename column plan_quantity to quantity;