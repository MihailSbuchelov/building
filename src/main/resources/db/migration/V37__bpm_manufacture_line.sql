alter table bpm_manufacture_line
    add column month_max_volume real not null default 0;

alter table bpm_article
    drop column volume,
    add column volume real not null default 0;
