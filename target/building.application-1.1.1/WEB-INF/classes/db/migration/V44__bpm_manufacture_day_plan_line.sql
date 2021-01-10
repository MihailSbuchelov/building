alter table bpm_manufacture_day_plan_line
    rename column comment to manager_comment;

alter table bpm_manufacture_day_plan_line
    add column employee_comment text;