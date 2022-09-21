package com.example.demo.utils;/*

 */

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 根据当前日期添加或减去的时间量，如getBeforeOrAfterDate(Date date, -3)表示日历时间减去3天
     * @param date
     * @param num
     * @return
     */
    public static Date getBeforeOrAfterDate(Date date, int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }
}
