CREATE SEQUENCE BPM_SECTOR_SEQUENCE;

create table BPM_SECTOR (
    ID integer NOT NULL,
    BPM_FLOOR_ID integer NOT NULL,
    BPM_BLOCK_ID integer NOT NULL,
    primary key (ID)
);

create index BPM_SECTOR_BPM_FLOOR_IDX on BPM_SECTOR(BPM_FLOOR_ID);
create index BPM_SECTOR_BPM_BLOCK_IDX on BPM_SECTOR(BPM_BLOCK_ID);

alter table BPM_SECTOR
    add constraint BPM_SECTOR_BPM_FLOOR_CONST
    foreign key (BPM_FLOOR_ID)
    references BPM_FLOOR(ID);
    
alter table BPM_SECTOR
    add constraint BPM_SECTOR_BPM_BLOCK_CONST
    foreign key (BPM_BLOCK_ID)
    references BPM_BLOCK(ID);

alter table BPM_PRODUCT drop column BPM_BLOCK_ID;
alter table BPM_PRODUCT drop column BPM_FLOOR_ID;

alter table BPM_PRODUCT add column BPM_SECTOR_ID integer not null;

create index BPM_PRODUCT_BPM_SECTOR_IDX on BPM_PRODUCT(BPM_SECTOR_ID);

alter table BPM_PRODUCT
    add constraint BPM_PRODUCT_BPM_SECTOR_CONST
    foreign key (BPM_SECTOR_ID)
    references BPM_SECTOR(ID);