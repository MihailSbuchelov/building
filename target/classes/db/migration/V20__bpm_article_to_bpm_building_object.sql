create sequence bpm_building_object_article_id_sequence;

create table bpm_building_object_article (
    id                      integer not null primary key,
    bpm_article_id          integer not null references bpm_article,
    bpm_building_object_id  integer not null references bpm_building_object
);

delete from bpm_product;

alter table bpm_product
    drop column bpm_article_id;

alter table bpm_product
    add column bpm_building_object_article_id integer not null references bpm_building_object_article;