package com.lu.hao.turtletime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Hao on 6/13/2015.
 */
public class ShowTurtleTimeActivity extends Activity {

    private Button mSetReminderButton;
    private ImageButton mSetReminderImageButton;

    private Button mCancelAlarms;

    // TODO: check if alarms have already been set once

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_turtle_time);

        // Get data from the other activity
        Intent activityThatCalled = getIntent();
        final int userID = activityThatCalled.getExtras().getInt("passingUserID");

        // New user turtle time object with the userID and the 5 times
        // final UserTurtleTime userTurtleTime = new UserTurtleTime();

        // Update the current week
        UserTurtleTime.updateUserIDTurtleTime();

        // Display two weeks worth of time turtles to the user
        displayTurtleTime(userID);

        mSetReminderButton = (Button)findViewById(R.id.set_reminder_button);
        mSetReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent SetTurtleTime = new Intent(ShowTurtleTimeActivity.this, SetupPopUpActivity.class);
                final int result = 1;
                // SetTurtleTime.putExtra("", userID);
                // startActivity(SetTurtleTime);
                // startActivityForResult(DisplayTurtleTime, result);
                if (SetReminderActivity.isAlarmSet()) {
                    Toast.makeText(ShowTurtleTimeActivity.this, "Reminders have already been set"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent SetTurtleTime = new Intent(ShowTurtleTimeActivity.this,
                            SetReminderActivity.class);
                    SetTurtleTime.putExtra("userID", userID);
                    SetTurtleTime.putExtra("currentDate", UserTurtleTime.getTurtleDate());
                    startActivity(SetTurtleTime);
                    // startActivityForResult(SetTurtleTime, result);
                }
            }
        });

        mSetReminderImageButton = (ImageButton)findViewById(R.id.set_alarm_image_button);
        mSetReminderImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(ShowTurtleTimeActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                final int result = 1;
                if (SetReminderActivity.isAlarmSet()) {
                    Toast.makeText(ShowTurtleTimeActivity.this, "Reminders have already been set"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Intent SetTurtleTime = new Intent(ShowTurtleTimeActivity.this,
                            SetReminderActivity.class);
                    SetTurtleTime.putExtra("userID", userID);
                    SetTurtleTime.putExtra("currentDate", UserTurtleTime.getTurtleDate());
                    startActivity(SetTurtleTime);

                }
            }
        });

        /*
        mCancelAlarms = (Button)findViewById(R.id.show_next_alarm);
        mCancelAlarms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelAllAlarms();
            }
        });
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_alarms_option:
                cancelAllAlarms();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void displayTurtleTime(int userID) {
        // Create reference to the dates
        TextView date_1a = (TextView)findViewById(R.id.date_1a);
        TextView date_1b = (TextView)findViewById(R.id.date_1b);
        TextView date_2a = (TextView)findViewById(R.id.date_2a);
        TextView date_2b = (TextView)findViewById(R.id.date_2b);

        // References to time TextViews
        TextView time_1a = (TextView)findViewById(R.id.time_1a);
        TextView time_1b = (TextView)findViewById(R.id.time_1b);
        TextView time_2a = (TextView)findViewById(R.id.time_2a);
        TextView time_2b = (TextView)findViewById(R.id.time_2b);

        time_1a.append((CharSequence) UserTurtleTime.getTurtleTime(userID).getFirstTimeString());
        time_1b.append((CharSequence) UserTurtleTime.getTurtleTime(userID).getSecondTimeString());

        String date = UserTurtleTime.getDateString();
        // Base case: first week
        appendToTextViewString(date_1a, date_1b, date, userID);

        // Get next week's date turtle time
        // TODO: Change back to 7
        UserTurtleTime.getTurtleDate().add(Calendar.DAY_OF_MONTH, 1);
        date = UserTurtleTime.getDateString();

        int tempID = userID;
        if (userID == 0) {
            tempID = 9;
        }
        else {
            tempID = userID - 1;
        }

        appendToTextViewString(date_2a, date_2b, date, tempID);
        time_2a.append((CharSequence) UserTurtleTime.getTurtleTime(tempID).getFirstTimeString());
        time_2b.append((CharSequence) UserTurtleTime.getTurtleTime(tempID).getSecondTimeString());

        // Revert so when pass the current week is used
        // TODO: Change back to 7
        UserTurtleTime.getTurtleDate().add(Calendar.DAY_OF_MONTH, -1);

    }

    public void appendToTextViewString(TextView date1, TextView date2, String date, int ID) {
        // Verify if statement portion
        // Originally was mTimes[ID]
        if (UserTurtleTime.getTurtleTime(ID).getSecondTimeString().compareTo("12:00 AM") == 0) {
            date1.append((CharSequence) date);
            UserTurtleTime.getTurtleDate().add(Calendar.DAY_OF_MONTH, 1);
            date = UserTurtleTime.getDateString();
            date2.append((CharSequence) date);
            UserTurtleTime.getTurtleDate().add(Calendar.DAY_OF_MONTH, -1);
        }
        else {
            date1.append((CharSequence) date);
            date2.append((CharSequence) date);
        }
    }

    public void cancelAllAlarms() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 11, intent, 0);
        alarmManager.cancel(pending);
        pending = PendingIntent.getBroadcast(this, 12, intent, 0);
        alarmManager.cancel(pending);

        /*
        // Cancels all the alarms in the array
        for (int i = 0; i < SetReminderActivity.getRequestCodes().length; i++) {
            pending = PendingIntent.getBroadcast(this, SetReminderActivity.getRequestCodes()[i],
                    intent, 0);
            alarmManager.cancel(pending);
        }*/

        // Cancels all the alarms in the array
        for (int i = 0; i < SetReminderActivity.getRequestCodes().length; i++) {
            String toast = "Index " + i;
            Toast.makeText(ShowTurtleTimeActivity.this, toast, Toast.LENGTH_SHORT).show();
            pending = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManager.cancel(pending);
        }

        Toast.makeText(ShowTurtleTimeActivity.this, "All reminders have been cancelled"
                , Toast.LENGTH_LONG).show();

    }

}
