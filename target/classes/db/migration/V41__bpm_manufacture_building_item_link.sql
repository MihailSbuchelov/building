create sequence bpm_manufacture_building_item_link_id_sequence;
create table bpm_manufacture_building_item_link (
    id integer primary key,
    bpm_manufacture_day_plan_item_id integer not null references bpm_manufacture_day_plan_item on delete restrict,
    bpm_building_plan_item_id integer not null references bpm_building_plan_item on delete restrict,
    quantity integer not null
);

create index bpm_manufacture_building_item_link_bpm_manufacture_plan_item_id_idx on bpm_manufacture_building_item_link(bpm_manufacture_day_plan_item_id);

create index bpm_manufacture_building_item_link_bpm_building_plan_item_idx on bpm_manufacture_building_item_link(bpm_building_plan_item_id);