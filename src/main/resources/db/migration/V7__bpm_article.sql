create sequence bpm_article_id_sequence;

create table bpm_article_type (
    id      integer primary key not null,
    name    text not null
);

create table bpm_article_kji (
    id      integer primary key not null,
    name    text not null
);

create table bpm_article (
    id      integer primary key not null,
    name    text not null,
    type_id integer not null,
    kji_id  integer not null,
    volume  text
);

alter table bpm_article
    add constraint bpm_article_2_bpm_article_type_fk
    foreign key (type_id)
    references bpm_article_type(id);

alter table bpm_article
    add constraint bpm_article_2_bpm_article_kji_fk
    foreign key (kji_id)
    references bpm_article_kji(id);