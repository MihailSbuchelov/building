CREATE SEQUENCE BPM_BLOCK_SEQUENCE;

create table BPM_BLOCK (
    ID integer NOT NULL,
    NAME varchar(2048) NOT NULL,
    primary key (ID)
);

/* Чтобы откатить миграцию
drop SEQUENCE BPM_BLOCK_SEQUENCE;
drop table BPM_BLOCK;
*/