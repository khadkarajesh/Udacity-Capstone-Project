package com.example.rajesh.udacitycapstoneproject.utils;

import java.text.SimpleDateFormat;

/**
 * Created by rajesh on 7/11/16.
 */
public class DateUtil {
    private static final String DATE_FORMAT = "MMM dd,yyy";

    public static String formatDate(java.util.Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }
}
