alter table bpm_manufacture_day_plan_line
    add column status text;

update bpm_manufacture_day_plan_line set status = 'Создана';

alter table bpm_manufacture_day_plan_line
    alter column status set not null;
