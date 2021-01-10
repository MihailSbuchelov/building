drop index BPM_PROCESS_CODE_IDX;
drop table BPM_PROCESS_CODE;

create table BPM_PROCESS_CODE (
    CODE varchar(5) NOT NULL,
    num integer NOT NULL,
    primary key (CODE)
);