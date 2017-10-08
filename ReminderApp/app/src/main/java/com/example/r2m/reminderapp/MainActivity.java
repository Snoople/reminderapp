package com.example.r2m.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editDate = (EditText)findViewById(R.id.editDate);
        final EditText editTime = (EditText)findViewById(R.id.editTime);
       final EditText SentCheck = (EditText)findViewById(R.id.SentCheck);
        final Button clicky = (Button)findViewById(R.id.Clicky);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        clicky.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              // SentCheck.setText("yes "+ editDate.getText() + " " + editTime.getText());
                startActivity(new Intent(MainActivity.this, Login.class));
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue("Hello, World!");
            }
        });

        // Read from the database
        DatabaseReference myReftest = database.getReference("test");
        myReftest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
}
