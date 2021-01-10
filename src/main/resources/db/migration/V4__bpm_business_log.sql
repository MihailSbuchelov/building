create table BPM_BUSINESS_LOG (
    ID integer NOT NULL,
    PROCESS_INSTANCE_ID varchar(64) NOT NULL,
    MESSAGE_TIME timestamp not null,
    MESSAGE text NOT NULL,
    primary key (ID)
);

create sequence BPM_BUSINESS_LOG_SEQUENCE;