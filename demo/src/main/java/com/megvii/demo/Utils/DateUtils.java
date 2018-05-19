package com.megvii.demo.Utils;

import java.text.SimpleDateFormat;

public class DateUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss.zzz");

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
}
