create sequence bpm_shipping_id_sequence;
create table bpm_shipping (
    id integer primary key,
    bpm_building_object_id integer not null references bpm_building_object on delete restrict,
    shipping_number integer not null,
    start_date timestamp not null,
    end_date timestamp not null,
    status text not null
);

create sequence bpm_shipping_shipping_number_sequence;

create sequence bpm_shipping_item_id_sequence;
create table bpm_shipping_item (
    id integer primary key,
    bpm_shipping_id integer not null references bpm_shipping on delete restrict,
    quantity integer not null
);