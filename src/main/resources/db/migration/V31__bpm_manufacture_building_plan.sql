delete from bpm_manufacture_building_plan_item;
delete from bpm_manufacture_building_plan;

alter table bpm_manufacture_building_plan
    drop column bpm_building_object_id,
    add column bpm_building_plan_id integer not null references bpm_building_plan;