CREATE SEQUENCE BPM_FLOOR_SEQUENCE;

create table BPM_FLOOR (
    ID integer NOT NULL,
    NAME varchar(2048) NOT NULL,
    BPM_BUILDING_OBJECT_ID integer NOT NULL,
    primary key (ID)
);

create index BPM_FLOOR_BPM_BUILDING_OBJECT_IDX on BPM_FLOOR(BPM_BUILDING_OBJECT_ID);

alter table BPM_FLOOR
    add constraint BPM_FLOOR_BPM_BUILDING_OBJECT_CONST
    foreign key (BPM_BUILDING_OBJECT_ID)
    references BPM_BUILDING_OBJECT(ID);
    
    
/* Чтобы откатить миграцию
drop SEQUENCE BPM_FLOOR_SEQUENCE;
drop table BPM_FLOOR;
*/