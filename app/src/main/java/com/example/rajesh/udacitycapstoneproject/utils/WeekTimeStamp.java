package com.example.rajesh.udacitycapstoneproject.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WeekTimeStamp {
    public static List<Date> getStartAndEndDate(Date date){
        List<Date> list = new ArrayList<>();
        list.add(getStartDate(date));
        list.add(getEndDate(date));
        return list;
    }

    public static Date getStartDate(Date date){
        Date startDate = getFirstDayOfWeek(date);
        return DayTimeStamp.getStartDate(startDate);
    }

    public static Date getEndDate(Date date){
        Date startDate = getLastDayOfWeek(date);
        return DayTimeStamp.getEndDate(startDate);
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar.getTime();
    }
}