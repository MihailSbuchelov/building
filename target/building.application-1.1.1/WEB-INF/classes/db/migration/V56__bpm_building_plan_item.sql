alter table bpm_building_plan_item add column production_reserve_quantity integer;
update bpm_building_plan_item set production_reserve_quantity = 0;
alter table bpm_building_plan_item alter column production_reserve_quantity set not null;

alter table bpm_manufacture_month_plan add column status text;
update bpm_manufacture_month_plan set status = 'Завершен';
alter table bpm_manufacture_month_plan alter column status set not null;