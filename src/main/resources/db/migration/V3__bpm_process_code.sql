create table BPM_PROCESS_CODE (
    PROCESS_INSTANCE_ID varchar(64) NOT NULL,
    CODE varchar(5) NOT NULL,
    id integer NOT NULL,
    primary key (PROCESS_INSTANCE_ID)
);

create index BPM_PROCESS_CODE_IDX on BPM_PROCESS_CODE(CODE);