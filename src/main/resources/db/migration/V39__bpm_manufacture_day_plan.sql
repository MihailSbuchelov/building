-- bpm_manufacture_day_plan
create sequence bpm_manufacture_day_plan_id_sequence;
create table bpm_manufacture_day_plan (
    id integer primary key,
    plan_day timestamp not null,
    bpm_manufacture_month_plan_id integer not null references bpm_manufacture_month_plan on delete restrict
);

-- bpm_manufacture_day_plan_line
create sequence bpm_manufacture_day_plan_line_id_sequence;
create table bpm_manufacture_day_plan_line (
    id integer primary key,
    bpm_manufacture_day_plan_id integer not null references bpm_manufacture_day_plan on delete restrict,
    bpm_building_object_id integer not null references bpm_building_object on delete restrict,
    bpm_manufacture_line_id integer not null references bpm_manufacture_line on delete restrict
);

-- bpm_manufacture_day_plan_item
create sequence bpm_manufacture_day_plan_item_id_sequence;
create table bpm_manufacture_day_plan_item (
    id integer primary key,
    bpm_manufacture_day_plan_line_id integer not null references bpm_manufacture_day_plan_line on delete restrict,
    bpm_article_id integer not null references bpm_article on delete restrict,
    plan_quantity integer not null
);