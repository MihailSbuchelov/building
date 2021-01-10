alter table bpm_building_plan add column status varchar default 'new';
create index bpm_building_plan_status_idx on bpm_building_plan(status);