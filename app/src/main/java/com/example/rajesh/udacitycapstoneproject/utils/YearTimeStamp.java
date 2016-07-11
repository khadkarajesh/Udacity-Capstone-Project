package com.example.rajesh.udacitycapstoneproject.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class YearTimeStamp {
    public static List<Date> getStartAndEndDate(Date date) {
        List<Date> list = new ArrayList<>();
        list.add(getStartDate(date));
        list.add(getEndDate(date));
        return list;
    }

    public static Date getStartDate(Date date){
        Date startDate = getFirstDayOfYear(date);
        return DayTimeStamp.getStartDate(startDate);
    }

    public static Date getEndDate(Date date){
        Date startDate = getLastDayOfYear(date);
        return DayTimeStamp.getEndDate(startDate);
    }

    public static Date getFirstDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getLastDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }
}
