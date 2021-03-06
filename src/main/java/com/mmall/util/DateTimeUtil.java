package com.mmall.util;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created By Zzyy
 **/
public class DateTimeUtil {

    public static final String STAND_FORMAT="yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STAND_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STAND_FORMAT);
    }

    public static void main(String[] args) {
        Date date =new Date();
        System.out.println(DateTimeUtil.dateToStr(date));
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, 8);

        System.out.println(DateTimeUtil.dateToStr(ca.getTime()));
        System.out.println(DateTimeUtil.strToDate("2020-04-03 17:47:00"));

        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime + currentTime%10);
    }
}
