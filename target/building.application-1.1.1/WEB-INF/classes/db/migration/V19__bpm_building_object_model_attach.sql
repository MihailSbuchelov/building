alter table BPM_BUILDING_OBJECT add column BPM_MODEL_ATTACHMENT_ID integer;

create index BPM_BUILDING_OBJECT_BPM_MODEL_ATTACHMENT_IDX on BPM_BUILDING_OBJECT(BPM_MODEL_ATTACHMENT_ID);

alter table BPM_BUILDING_OBJECT
    add constraint BPM_BUILDING_OBJECT_BPM_MODEL_ATTACHMENT_CONST
    foreign key (BPM_MODEL_ATTACHMENT_ID)
    references BPM_ATTACHMENT(ID);