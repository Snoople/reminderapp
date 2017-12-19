package com.example.r2m.reminderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class Reminder extends AppCompatActivity implements View.OnClickListener{

    TimePicker editTime;
    Button buttonTimePicker;
    private int toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
toggle = 1;
        editTime = (TimePicker)findViewById(R.id.editTime);
        buttonTimePicker=(Button)findViewById(R.id.buttonTimePicker);

buttonTimePicker.setOnClickListener(this);

//        editDate.setVisibility(View.VISIBLE);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.action_favorite:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
@Override
public void onClick(View v) {
    if (v == buttonTimePicker) {
        if(toggle==1) {
            editTime.setVisibility(View.VISIBLE);
            toggle=0;
        }
        else{
            editTime.setVisibility(View.GONE);
            toggle=1;
        }

    }
}
}
