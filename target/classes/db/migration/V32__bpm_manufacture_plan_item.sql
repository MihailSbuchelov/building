drop sequence bpm_manufacture_building_plan_item_id_sequence;

create sequence bpm_manufacture_plan_item_id_sequence;

delete from bpm_manufacture_building_plan_item;

alter table bpm_manufacture_building_plan_item
    rename to bpm_manufacture_plan_item;

alter table bpm_manufacture_plan_item
    drop column bpm_manufacture_building_plan_id,
    add column bpm_manufacture_plan_id integer not null references bpm_building_plan,
    add column bpm_building_object_id integer not null references bpm_building_object;
