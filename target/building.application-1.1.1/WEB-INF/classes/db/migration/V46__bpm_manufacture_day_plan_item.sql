-- bpm_manufacture_day_plan_item
create sequence bpm_manufacture_day_plan_item_id_sequence;
create table bpm_manufacture_day_plan_item (
    id integer primary key,
    bpm_manufacture_day_plan_line_id integer not null references bpm_manufacture_day_plan_line on delete restrict,
    bpm_article_id integer not null references bpm_article on delete restrict,
    quantity integer not null
);