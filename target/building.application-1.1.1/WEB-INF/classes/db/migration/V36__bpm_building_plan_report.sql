create sequence bpm_building_plan_report_id_sequence;

create table bpm_building_plan_report (
    id integer not null primary key,
    report_date timestamp not null,
    bpm_building_plan_id integer not null references bpm_building_plan
);

create index bpm_building_plan_report_bpm_building_plan_idx on bpm_building_plan_report(bpm_building_plan_id);


create sequence bpm_building_plan_report_sector_id_sequence;

create table bpm_building_plan_report_sector (
    id integer not null primary key,
    bpm_building_plan_report_id integer not null references bpm_building_plan_report,
    bpm_building_plan_sector_id integer not null references bpm_building_plan_sector,
    completion integer not null default 0
);

create index bpm_building_plan_report_sector_bpm_building_plan_sector_idx on bpm_building_plan_report_sector(bpm_building_plan_sector_id);
create index bpm_building_plan_report_sector_bpm_building_plan_report_idx on bpm_building_plan_report_sector(bpm_building_plan_report_id);