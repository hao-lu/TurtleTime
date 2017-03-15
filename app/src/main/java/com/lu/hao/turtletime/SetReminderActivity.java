package com.lu.hao.turtletime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Hao on 6/21/2015.
 */
public class SetReminderActivity extends Activity {

    private TextView mMinutesBeforeTextView;
    private Button mSetNotificationButton;
    private CheckBox mAlwaysRemindCheckBox;
    // Alarm request codes 2 for the individual and 5 * 2 for the repeat
    private static int[] mAlarmRequestCode = new int[10];
    private int mRequestCodeIndex = 0;
    private static boolean mAllAlarmSet = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        Intent activityThatCalled = getIntent();
        final int userID = activityThatCalled.getExtras().getInt("userID");
        final Calendar currentDate = (Calendar) activityThatCalled.getExtras().get("currentDate");

        // Toast to check progress
        //SimpleDateFormat timeFormat = new SimpleDateFormat("MMMM d, yyyy - h:mm");
        //String eventDate = timeFormat.format(currentDate.getTime());
        //Toast.makeText(SetReminderActivity.this, eventDate, Toast.LENGTH_SHORT).show();

        // TODO: finish the check box checking to see which method will run
        mAlwaysRemindCheckBox = (CheckBox) findViewById(R.id.always_remind_checkbox);

        // Drop down for the time the user wants to be reminded before
        mMinutesBeforeTextView = (TextView) findViewById(R.id.minutes_before);
        // When the minutes before text is clicked, pop up
        mMinutesBeforeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final int result = 1;
                Intent SetTime = new Intent(SetReminderActivity.this, SetupPopUpActivity.class);
                startActivityForResult(SetTime, result);*/

                PopupMenu popup = new PopupMenu(SetReminderActivity.this, mMinutesBeforeTextView);
                popup.getMenuInflater().inflate(R.menu.popup_menu_time, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mMinutesBeforeTextView.setText(item.getTitle());
                        return true;
                    }
                });
                popup.show();

            }
        });

        mSetNotificationButton = (Button) findViewById(R.id.set_notification_button);
        mSetNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlwaysRemindCheckBox.isChecked()) {
                    Toast.makeText(SetReminderActivity.this, "All reminders set",
                            Toast.LENGTH_SHORT).show();
                    setAllReminders(v, userID, currentDate);
                    // All alarms have been set
                    mAllAlarmSet = true;
                } else {
                    Toast.makeText(SetReminderActivity.this, "One reminder set",
                            Toast.LENGTH_SHORT).show();
                    setAlarm(v, currentDate, userID);
                }

                finish();
            }
        });

    }

    public int getMinutesBefore() {
        int timeBefore;
        String minutesBefore[] = mMinutesBeforeTextView.getText().toString().split(" ");
        // On time then return 0
        if (minutesBefore[0].compareTo("On") == 0) {
            return 0;
        } else if (minutesBefore[1].compareTo("minutes") == 0) {
            // Gets the time in milliseconds
            timeBefore = Integer.parseInt(minutesBefore[0]) * 1000 * 60;
        }
        // Hour equation
        else {
            timeBefore = Integer.parseInt(minutesBefore[0]) * 1000 * 60 * 60;
        }
        Toast.makeText(SetReminderActivity.this, mMinutesBeforeTextView.getText().toString(),
                Toast.LENGTH_SHORT).show();
        return timeBefore;
    }

    /*
    // Basic set alarm for a 5 second alarm reminder
    public void setAlarm(View view) {
        Long alertTime = new GregorianCalendar().getTimeInMillis()+ 5*1000;
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
    */

    // Custom time reminder
    public void setAlarm(View view, Calendar currentDate, int userID) {
        // Print to check
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy h:mm a");

        // Time of the event in milliseconds
        // Long alertTime = currentDate.getTimeInMillis();

        // Set first turtle time
        Long alertTime = setFirstTurtleDateAndTime(userID, currentDate).getTimeInMillis() -
                getMinutesBefore();

        Calendar test = new GregorianCalendar();
        test.setTimeInMillis(alertTime);
        String print = simpleDateFormat.format(test.getTime());
        Toast.makeText(SetReminderActivity.this, print, Toast.LENGTH_SHORT).show();

        // Intent for the AlertReceiver class to make a notification
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        // AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 11, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        // Set second turtle time
        alertTime = setSecondTurtleDateAndTime(userID, currentDate).getTimeInMillis() -
                getMinutesBefore();
        // Change the requestCode to 2 so they don't overwrite each other when set
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 12, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

    }

    public void setAllReminders(View view, int userID, Calendar currentDate) {
        for (int i = 0; i < UserTurtleTime.getNumOfTurtleTimes(); i++) {
            setTurtleDateAndTime(view, userID, currentDate);
            // Updates to the next turtle time
            if (userID == 0) {
                userID = 9;
            } else {
                --userID;
            }
            // setTurtleDateAndTime(view, userID, currentDate);
            currentDate.add(Calendar.DATE, 7);
        }
    }

    public void setTurtleDateAndTime(View view, int userID, Calendar currentDate) {
        // Sets the current date with the first turtle time minute
        currentDate = setFirstTurtleDateAndTime(userID, currentDate);
        // Call the the method that sets the repeating alarm for every 5th week for the first time
        setAlarmRepeating(view, currentDate);

        // Checks if the date is on midnight to add a day to the calendar
        if (UserTurtleTime.getTurtleTime(userID).getSecondTimeString()
                .compareTo("12:00 AM") == 0) {
            currentDate.add(Calendar.DATE, 1);
        }

        // Sets the current date with the second turtle time minute
        currentDate = setSecondTurtleDateAndTime(userID, currentDate);
        // Call the the method that sets the repeating alarm for every 5th week for the second time
        setAlarmRepeating(view, currentDate);

        // Have to go back a date because currentDate is final and altering needs to be undone
        if (UserTurtleTime.getTurtleTime(userID).getSecondTimeString()
                .compareTo("12:00 AM") == 0) {
            currentDate.add(Calendar.DATE, -1);
        }
    }

    public Calendar setFirstTurtleDateAndTime(int userID, Calendar currentDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy h:mm a");
        // Gets the turtle time based on the user ID
        TurtleTime turtleTime = UserTurtleTime.getTurtleTime(userID);
        // Sets the current date with the first turtle time hour
        currentDate.set(Calendar.HOUR_OF_DAY, turtleTime.getFirstTime().get(Calendar.HOUR_OF_DAY));
        // Sets the current date with the first turtle time minute
        currentDate.set(Calendar.MINUTE, turtleTime.getFirstTime().get(Calendar.MINUTE));
        // Toast for the alarm
        String date = simpleDateFormat.format(currentDate.getTime());
        Toast.makeText(SetReminderActivity.this, date, Toast.LENGTH_SHORT).show();
        return currentDate;
    }

    public Calendar setSecondTurtleDateAndTime(int userID, Calendar currentDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy h:mm a");
        // Gets the turtle time based on the user ID
        TurtleTime turtleTime = UserTurtleTime.getTurtleTime(userID);
        // Sets the current date with the second turtle time hour
        currentDate.set(Calendar.HOUR_OF_DAY, turtleTime.getSecondTime().get(Calendar.HOUR_OF_DAY));
        // Sets the current date with the second turtle time minute
        currentDate.set(Calendar.MINUTE, turtleTime.getSecondTime().get(Calendar.MINUTE));
        // Toast for the alarm being set
        String date = simpleDateFormat.format(currentDate.getTime());
        Toast.makeText(SetReminderActivity.this, date, Toast.LENGTH_SHORT).show();
        return currentDate;
    }

    public void setAlarmRepeating(View view, Calendar turtleTime) {
        Long alertTime = turtleTime.getTimeInMillis() - getMinutesBefore();
        Intent alertIntent = new Intent(this, AlertReceiver.class);

        // Unique requestCode so alarms aren't overwritten
        // final int requestCode = (int)System.currentTimeMillis();
        final int requestCode = mRequestCodeIndex;
        // Saves the code into an array to be later cancel by the user request
        /*
        mAlarmRequestCode[mRequestCodeIndex] = requestCode;
        mRequestCodeIndex++;
        if (mRequestCodeIndex == 9)
            mRequestCodeIndex = 0;
            */
        // Turtle times repeats every 5 weeks
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alertTime,
                AlarmManager.INTERVAL_DAY * 5,
                PendingIntent.getBroadcast(this, requestCode, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        // String s = "This is request code: " + mRequestCodeIndex;
        // Toast.makeText(SetReminderActivity.this,s ,Toast.LENGTH_SHORT).show();

        mRequestCodeIndex++;
    }

    /*
    // TODO: cancel alarms
    public void cancelAllAlarms() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 2, intent, 0);
        alarmManager.cancel(pending);

        // Cancels all the alarms in the array
        for (int i = 0; i < mAlarmRequestCode.length; i++) {
            pending = PendingIntent.getBroadcast(this, mAlarmRequestCode[i], intent, 0);
            alarmManager.cancel(pending);
        }
        // All alarms have been canceled
        mAllAlarmSet = false;

    }
    */

    public static int[] getRequestCodes() {
        return mAlarmRequestCode;
    }

    // Checks whether or not the alarms have already been set
    public static boolean isAlarmSet() {
        return mAllAlarmSet;
    }

}
