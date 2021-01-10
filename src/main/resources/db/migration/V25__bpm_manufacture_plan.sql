delete from bpm_manufacture_plan_item;
delete from bpm_manufacture_plan;

alter table bpm_manufacture_plan
    drop column month;

alter table bpm_manufacture_plan
    add column plan_month date not null;