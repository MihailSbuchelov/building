alter table bpm_shipping_item
    add bpm_building_plan_item_id integer not null references bpm_building_plan_item on delete restrict;