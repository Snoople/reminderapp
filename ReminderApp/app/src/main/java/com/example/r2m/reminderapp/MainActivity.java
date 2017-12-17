package com.example.r2m.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        startActivity(new Intent(MainActivity.this, Login.class));

        /**
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                   // UserDetails.username = ;
                    database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot imageSnapshot: dataSnapshot.getChildren()) {
                                  Log.d("snapshot", imageSnapshot.toString());
                        //        Log.d("stuff", "onAuthStateChanged:signed_in:" + imageSnapshot.child("email").getValue().toString());
                        //        Log.d("stuff", "onAuthStateChanged:signed_in:" + user.getEmail());
                                if(imageSnapshot.child("email").getValue().toString().equals(user.getEmail()))
                                {
                                    UserDetails.username =  imageSnapshot.getKey().toString();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), Users.class));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });

                } else {
                    // User is signed out
                    Log.d("stuff", "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, Login.class));
                }
                // ...
            }
        };

**/
    }
    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        //if (mAuthListener != null) {
          //  mAuth.removeAuthStateListener(mAuthListener);
       // }
    }
}





/*TODO LIST
1: Comments everything
5: chat scroll and a lot more, no auto scroll
5: stay logged in
6: more notifcation working shit
7: add confrim password
8: log out button
9: Token randomly changes
 */

