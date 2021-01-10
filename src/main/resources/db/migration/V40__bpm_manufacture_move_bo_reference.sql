alter table bpm_manufacture_day_plan_line 
drop column bpm_building_object_id;

alter table bpm_manufacture_day_plan_item
add column bpm_building_object_id integer not null references bpm_building_object on delete restrict;

alter table bpm_manufacture_day_plan_item
add column quantity integer not null;