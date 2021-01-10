package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import ru.everybit.bzkpd_bsk.application.service.utils.BpmUtils;

import java.util.Date;

/**
 * Исправлена ошибка с отображением даты без учета таймзоны пользователя
 */
public class BpmTable extends Table {
    private static final long serialVersionUID = 1L;
    // model
    private String dateFormat = "dd.MM.yyyy HH:mm";

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        if (property != null && property.getType().equals(Date.class)) {
            return BpmUtils.formatDateWithTimezone((Date) property.getValue(), dateFormat);
        }
        return super.formatPropertyValue(rowId, colId, property);
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
