package com.example.r2m.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editDate = (EditText)findViewById(R.id.editDate);
        final EditText editTime = (EditText)findViewById(R.id.editTime);
        final EditText SentCheck = (EditText)findViewById(R.id.SentCheck);
        final Button clicky = (Button)findViewById(R.id.Clicky);
        Log.d("FCM", "Instance ID: " + FirebaseInstanceId.getInstance().getToken());


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                    startActivity(new Intent(getApplicationContext(), Users.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        clicky.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              // SentCheck.setText("yes "+ editDate.getText() + " " + editTime.getText());
                startActivity(new Intent(MainActivity.this, Login.class));
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue("Hello, World!");

                //this prints out the token in Console
                //e.g.  D/App: Token [dA82roySUyA:APA91bFfcinKlKESFzkXUSaZKWSf0Hm_hlecRr3rQ3TYOdm0i5r0HeCoWxhOaXUXqADSaoCQHuRm2BdOZVfm20O0RFCoTUzBaw0JyUjwrOAbJ3gJtp_3DGUWZtzksulx6iNWURhFwckt]

                String tkn = FirebaseInstanceId.getInstance().getToken();
                Toast.makeText(MainActivity.this, "Current token ["+tkn+"]", Toast.LENGTH_LONG).show();
                Log.d("App", "Token ["+tkn+"]");

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
