package com.lu.hao.turtletime;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Hao on 6/11/2015.
 */
public class TurtleTime {
    private Calendar mFirstTime = new GregorianCalendar(2015, 6, 13, 00, 00);
    private Calendar mSecondTime = new GregorianCalendar(2015, 6, 13, 00, 00);

    public TurtleTime(int time1, int time2) {
        mFirstTime.set(mFirstTime.HOUR_OF_DAY, time1);
        mFirstTime.set(mFirstTime.MINUTE, 00);
        mSecondTime.set(mSecondTime.HOUR_OF_DAY, time2);
        mSecondTime.set(mSecondTime.MINUTE, 00);
    }

    public String getFirstTimeString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String turtleTime = timeFormat.format(mFirstTime.getTime());
        return turtleTime;
    }

    public String getSecondTimeString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String turtleTime = timeFormat.format(mSecondTime.getTime());
        return turtleTime;
    }

    public Calendar getFirstTime() {
        return mFirstTime;
    }

    public Calendar getSecondTime() {
        return mSecondTime;
    }

}
