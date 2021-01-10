-- BpmAttachment
create sequence bpm_attachment_id_sequence;

create table bpm_attachment (
    id  integer primary key not null
);

-- BpmAttachmentFile
create sequence bpm_attachment_file_id_sequence;

create table bpm_attachment_file (
    id              integer primary key not null,
    attachment_id   integer not null,
    file_name       text not null,
    create_date     timestamp not null
);

alter table bpm_attachment_file
    add constraint bpm_attachment_file_2_bpm_attachment_fk
    foreign key (attachment_id)
    references bpm_attachment(id);