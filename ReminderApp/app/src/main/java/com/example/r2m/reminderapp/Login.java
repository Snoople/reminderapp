package com.example.r2m.reminderapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Login extends AppCompatActivity {
    TextView registerUser;
    EditText getUsername, getPassword;
    Button loginButton;
    private String user, pass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    DatabaseReference reference;
    FirebaseDatabase database;
    private String email;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = (TextView) findViewById(R.id.register);
        getUsername = (EditText) findViewById(R.id.username);
        getPassword = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

       database = FirebaseDatabase.getInstance();


        progressDialog = new ProgressDialog(this);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }

        });
    }

    public void userLogin() {
        String username = getUsername.getText().toString().trim();
        String password = getPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter email plox", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter secret plox", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("checking your secrets");
        progressDialog.show();
        reference = database.getReference("users/" + username);

        ValueEventListener pullListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("E_VALUE", "Data : " + dataSnapshot.getValue());
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                if(map!=null) {
                    email = map.get("email").toString();
                }
            };
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(pullListener);




//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//               // Map map = dataSnapshot.getValue(Map.class);
//                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                email = map.get("email").toString();
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError firebaseError) {
//
//            }
//        });





        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            //start afterwards
                            UserDetails.username = user;
                            finish();
                            startActivity(new Intent(getApplicationContext(), Users.class));
                        } else {
                            Toast.makeText(Login.this, "shits on fire", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

