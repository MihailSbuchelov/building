update bpm_manufacture_day_plan_line_building_object_item set stock_quantity = 0 where stock_quantity is null;
ALTER TABLE bpm_manufacture_day_plan_line_building_object_item ALTER COLUMN stock_quantity SET NOT NULL;
