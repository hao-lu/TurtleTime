package com.lu.hao.turtletime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    private Button mEnterKey;
    private EditText mUserID;
    private TextView mUserIDInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // References to widgets
        mUserID = (EditText)findViewById(R.id.user_ID);
        mUserIDInput = (TextView)findViewById(R.id.ID_Digit_Input);
        mEnterKey = (Button)findViewById(R.id.enter_button);

        // On click listener for the enter button
        mEnterKey.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, R.string.enter_toast, Toast.LENGTH_SHORT).show();
                // mEnterKey.setVisibility(v.GONE);
                // mUserID.setVisibility(v.GONE);
                // mUserIDInput.setVisibility(v.GONE);
                // mDisplayTimes.setVisibility(v.VISIBLE);

                String checkEmpty = mUserID.getText().toString();
                if (checkEmpty.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter your 6th digit user ID digit",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    int userID = Integer.parseInt((String.valueOf(mUserID.getText())));
                    if (userID >= 0 && userID <= 9) {
                        onEnterButtonClick(v, userID);
                    } else {
                        onEnterButtonClickInvalid(v);
                    }
                }

            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    public void onEnterButtonClick(View view, int userID) {
        // String userID = String.valueOf(mUserID.getText());
        // int userID = Integer.parseInt(String.valueOf(mUserID.getText()));

        // Toast: to print out the user's ID
        // String yourResponse = "Your 6th ID digit is " + userID;
        // Toast.makeText(this,yourResponse,Toast.LENGTH_SHORT).show();

        Intent DisplayTurtleTime = new Intent(MainActivity.this, ShowTurtleTimeActivity.class);

        // final int result = 1;
        DisplayTurtleTime.putExtra("passingUserID", userID);
        startActivity(DisplayTurtleTime);
        // If you want information back from the other activity
        // startActivityForResult(DisplayTurtleTime, result);

    }

    public void onEnterButtonClickInvalid(View view) {
        String invalidInput = "The 6th input ID range is from 0 to 9. Try agian.";
        Toast.makeText(this, invalidInput, Toast.LENGTH_SHORT).show();

    }
}
