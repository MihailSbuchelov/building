alter table bpm_manufacture_day_plan_line_building_object_item
    rename column quantity to plan_quantity;

alter table bpm_manufacture_day_plan_line_building_object_item
    add column quantity integer;

update bpm_manufacture_day_plan_line_building_object_item set quantity = 0;

alter table bpm_manufacture_day_plan_line_building_object_item
    alter column quantity set not null;