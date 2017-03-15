package com.lu.hao.turtletime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Hao on 6/20/2015.
 */
public class SetupPopUpActivity extends Activity{

    private Button SetButton;
    private LinearLayout TintBackground;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_setup_time_pop_up_window);
        setContentView(R.layout.activity_setup_time_pop_up_window);

        /*
        Intent SendMinutesBefore = new Intent();
        SendMinutesBefore.putExtra("UsersName", userName);
        setResult(RESULT_OK, SendMinutesBefore);
        finish();
        */



    }
}


/*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_time_pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .8));
 */