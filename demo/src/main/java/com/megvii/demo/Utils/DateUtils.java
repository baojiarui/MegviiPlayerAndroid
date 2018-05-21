package com.megvii.demo.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    private static DateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss.zzz");
    private static DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(long time){
        return sdf.format(time);
    }

    public static String getFormatTime(long time){
        String timeStr = null;
        if(time > 0){
            timeStr = format(time);
        }
        return timeStr;
    }

    public static long getTimestamp(String dateStr){
        java.util.Date date = null;
        try {
            date = sdf2.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();

        return timestamp;
    }
}
