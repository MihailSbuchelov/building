alter table BPM_BUILDING_PLAN_SECTOR add column completion integer NOT NULL default 0;

alter table BPM_BUILDING_PLAN_ITEM add column PRODUCTION_QUANTITY integer NOT NULL default 0;
alter table BPM_BUILDING_PLAN_ITEM add column STOCK_QUANTITY integer NOT NULL default 0;
alter table BPM_BUILDING_PLAN_ITEM add column LOCAL_STOCK_QUANTITY integer NOT NULL default 0;
