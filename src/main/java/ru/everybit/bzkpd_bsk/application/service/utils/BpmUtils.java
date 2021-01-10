package ru.everybit.bzkpd_bsk.application.service.utils;

import com.vaadin.server.Page;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BpmUtils {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MM.yyyy");
    public static String formatDate(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static String formatDateWithTimezone(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        int rawTimezoneOffset = Page.getCurrent().getWebBrowser().getRawTimezoneOffset();
        String[] availableIDs = TimeZone.getAvailableIDs(rawTimezoneOffset);
        TimeZone timeZone = TimeZone.getTimeZone(availableIDs[0]);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }
    
    public static Date prepareDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Object formatMonth(Date date) {
        return MONTH_DATE_FORMAT.format(date);
    }
}
