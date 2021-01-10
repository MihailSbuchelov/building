create sequence bpm_building_plan_id_sequence;
create table bpm_building_plan (
    id integer not null primary key
);

create sequence bpm_building_plan_section_id_sequence;

create table bpm_building_plan_section (
    id integer not null primary key,
    bpm_floor_id integer not null references bpm_floor,
    bpm_block_id integer not null references bpm_block,
    bpm_building_plan_id integer not null references bpm_building_plan
);

create index bpm_building_plan_section_bpm_floor_idx on bpm_building_plan_section(bpm_floor_id);
create index bpm_building_plan_section_bpm_block_idx on bpm_building_plan_section(bpm_block_id);
create index bpm_building_plan_section_bpm_building_plan_idx on bpm_building_plan_section(bpm_building_plan_id);

create sequence bpm_building_plan_item_id_sequence;

create table bpm_building_plan_item (
    id integer not null primary key,
    bpm_article_id integer not null,
    quantity integer not null default 0,
    bpm_building_plan_id integer not null references bpm_building_plan
);

create index bpm_building_plan_item_bpm_building_plan_idx on bpm_building_plan_item(bpm_building_plan_id);
create index bpm_building_plan_item_bpm_article_idx on bpm_building_plan_item(bpm_article_id);
