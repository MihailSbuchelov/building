CREATE SEQUENCE BPM_PRODUCT_SEQUENCE;

create table BPM_PRODUCT (
    ID integer NOT NULL,
    BPM_FLOOR_ID integer NOT NULL,
    BPM_BLOCK_ID integer NOT NULL,
    BPM_ARTICLE_ID integer NOT NULL,
    QUANTITY integer NOT NULL DEFAULT 0,
    primary key (ID)
);

create index BPM_PRODUCT_BPM_FLOOR_IDX on BPM_PRODUCT(BPM_FLOOR_ID);
create index BPM_PRODUCT_BPM_BLOCK_IDX on BPM_PRODUCT(BPM_BLOCK_ID);
create index BPM_PRODUCT_BPM_ARTICLE_IDX on BPM_PRODUCT(BPM_ARTICLE_ID);

alter table BPM_PRODUCT
    add constraint BPM_PRODUCT_BPM_FLOOR_CONST
    foreign key (BPM_FLOOR_ID)
    references BPM_FLOOR(ID);
    
alter table BPM_PRODUCT
    add constraint BPM_PRODUCT_BPM_BLOCK_CONST
    foreign key (BPM_BLOCK_ID)
    references BPM_BLOCK(ID);
    
alter table BPM_PRODUCT
    add constraint BPM_PRODUCT_BPM_ARTICLE_CONST
    foreign key (BPM_ARTICLE_ID)
    references BPM_ARTICLE(ID);

/* Чтобы откатить миграцию
drop SEQUENCE BPM_PRODUCT_SEQUENCE;
drop table BPM_PRODUCT;
*/