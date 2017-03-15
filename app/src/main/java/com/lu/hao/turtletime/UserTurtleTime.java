package com.lu.hao.turtletime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Hao on 7/23/2015.
 */
public class UserTurtleTime {
    // Use to compare the current week to the base week; the base week is use to subtract the current
    // week from the current week to determine the user's turtle time
    private static Calendar mBaseWeek = new GregorianCalendar(2015, 6, 27);
    // Gets the next possible Monday date
    private static Calendar mTurtleDate = getCurrentMonday();
    // Turtle time associated with the 6th digit ID
    private static int[] mSixthDigitID = {3, 2, 1, 0, 4, 3, 2, 1, 0, 4};
    // An array of all the possible turtle times
    private static TurtleTime[] mTurtleTimes = {new TurtleTime(14, 24), new TurtleTime(12, 22)
            ,new TurtleTime(10, 20), new TurtleTime(8, 18), new TurtleTime(6, 16)};
                                                //8 18
    public static String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        String date = dateFormat.format(mTurtleDate.getTime());
        return date;
    }

    public static TurtleTime getTurtleTime(int userID) {
        return mTurtleTimes[mSixthDigitID[userID]];
    }

    //TODO: Change back to Monday
    private static Calendar getCurrentMonday() {
        Calendar currentMonday = Calendar.getInstance();
        while (currentMonday.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            currentMonday.add(Calendar.DAY_OF_MONTH, 1);
        }
        return currentMonday;
    }

    public static Calendar getTurtleDate() {
        return mTurtleDate;
    }

    public static void updateUserIDTurtleTime() {
        int updateNum = mTurtleDate.get(Calendar.WEEK_OF_YEAR)
                - mBaseWeek.get(Calendar.WEEK_OF_YEAR);
        if (updateNum == 0)
            return;
        else {
            // Loop numbers of week ahead of the base week
            for (int weeksAhead = 0; weeksAhead < updateNum; weeksAhead++) {
                for (int userID = 0 ; userID < mSixthDigitID.length; userID++) {
                    // If less than 4, simply add to the current
                    if (mSixthDigitID[userID] < 4) {
                        mSixthDigitID[userID]++;
                    }
                    // If the userID is at the 4 rotation then set it to 0
                    else if (mSixthDigitID[userID] == 4) {
                        mSixthDigitID[userID] = 0;
                    }
                }
            }
        }

    }

    public static int getNumOfTurtleTimes() {
        return mTurtleTimes.length;
    }
}

