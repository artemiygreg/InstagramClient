package ru.instagramclient.Service.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 24.02.15.
 */
public class DateService {
    public static final String DATE_FORMAT_RU = "dd.MM.yyyy";
    public static final String TIME_FORMAT_RU = "HH:mm";
    public static final String DATE_FORMAT_FOR_RU_LOCALE = "dd MMMM y г.";
    public static final String DATE_AND_TIME_FORMAT_RU = "dd.MM.yyyy HH:mm";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final Long COUNT_MS_IN_DAY = 86400000L;
    private static DateFormat df;


    public static Date parseStringDate(String dateString) {
        Date date = null;
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parseStringDate(String dateString, String format) {
        Date date = null;
        DateFormat df = new SimpleDateFormat(format);
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDateToString(Date date) {
        if (df == null) df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static String formatDateToString(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date getCurrentDateAndTime() {
        return Calendar.getInstance().getTime();

    }
    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }
    public static String convertTimestampToString(long timestamp){
        DateFormat dateFormat = new SimpleDateFormat(DATE_AND_TIME_FORMAT_RU);
        Date date = new Date(timestamp);
        return dateFormat.format(date);
    }
    public static String convertTimestampToString(long timestamp, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(timestamp));
    }
    public static Date convertTimestampToDate(long timestamp){
        return new Date(timestamp);
    }
}