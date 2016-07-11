package com.example.rajesh.udacitycapstoneproject.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthTimeStamp {
    public static List<Date> getStartAndEndDate(Date date){
        List<Date> list = new ArrayList<>();
        list.add(getStartTimeStamp(date));
        list.add(getEndTimeStamp(date));
        return list;
    }

    public static Date getStartTimeStamp(Date date){
        Date startDate = getFirstDayOfMonth(date);
        return DayTimeStamp.getStartDate(startDate);
    }

    public static Date getEndTimeStamp(Date date){
        Date startDate = getLastDayOfMonth(date);
        return DayTimeStamp.getEndDate(startDate);
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }
}