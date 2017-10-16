package com.example.r2m.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(MainActivity.this, Login.class));
    }
}


/*TODO LIST
1: Comments everything
2:FCM token
3: duplicate user
4: duplicate email
5: chat scroll and a lot more, no auto scroll
5: chat scroll and a lot more, no auto scroll
5: chat scroll and a lot more, no auto scroll
5: chat scroll and a lot more, no auto scroll
5: chat scroll and a lot more, no auto scroll
5: chat scroll and a lot more, no auto scroll

 */

