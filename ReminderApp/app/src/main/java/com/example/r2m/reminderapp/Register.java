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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    EditText editUsername, editPassword, UsernameEdit;
    Button registerButton;
    String user, pass;
    TextView login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private String TAG;
    private FirebaseDatabase database;
    private DatabaseReference reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = (EditText)findViewById(R.id.username);
        editPassword = (EditText)findViewById(R.id.password);
        UsernameEdit = (EditText)findViewById(R.id.UsernameEdit);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        //Firebase setup
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                    startActivity(new Intent(Register.this, Users.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    finish();
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                // ...
            }
        };

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        final String email = editUsername.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        final String username = UsernameEdit.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email plox", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter secret plox", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "username is blank dum dumb", Toast.LENGTH_SHORT).show();
            return;
        }
        reference1 = database.getReference("users");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username.toLowerCase())) {
                    Toast.makeText(Register.this, "username already taken", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    registerUserFirbase(username, email, password);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }
    public void registerUserFirbase(final String username,final String email, final String password){


        Log.d("App", email +" " + password);
        progressDialog.setMessage("stealing your data");
        progressDialog.show();
                                   
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Login Failed Nub",
                                    Toast.LENGTH_SHORT).show();

                            progressDialog.hide();
                        } else {


                            Map<String, String> map = new HashMap<String, String>();
                            map.put("email",email);
                            map.put("token", FirebaseInstanceId.getInstance().getToken());
                            map.put("nickname", username);
                            //   myRef.child(user).child("token").setValue(refreshedToken);
                            reference1.child(username.toLowerCase()).setValue(map);


                            Toast.makeText(Register.this, "registered In", Toast.LENGTH_SHORT).show();


                            progressDialog.hide();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }

                        // ...
                    }
                });

    }
}