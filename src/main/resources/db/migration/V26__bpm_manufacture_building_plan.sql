alter sequence bpm_manufacture_plan_sequence
    rename to bpm_manufacture_plan_id_sequence;

create sequence bpm_manufacture_building_plan_id_sequence;

create table bpm_manufacture_building_plan (
    id integer primary key,
    bpm_manufacture_plan_id integer not null references bpm_manufacture_plan,
    bpm_building_object_id integer not null references bpm_building_object
);

delete from bpm_manufacture_plan_item;

alter table bpm_manufacture_plan_item
    drop column bpm_manufacture_plan_id;

alter table bpm_manufacture_plan_item
    add column bpm_manufacture_building_plan_id integer not null references bpm_manufacture_building_plan;

alter sequence bpm_manufacture_plan_item_sequence
    rename to bpm_manufacture_building_plan_item_id_sequence;

alter table bpm_manufacture_plan_item
    rename to bpm_manufacture_building_plan_item;