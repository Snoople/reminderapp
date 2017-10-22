package com.example.r2m.reminderapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        scrollToBottom();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Keeps the message box closed when you open the app initially
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        reference1 = database.getReference("messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = database.getReference("messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        reference1.getDatabase();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageArea.getText().toString();
                //scrollToBottom();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    // Get a handler that can be used to post to the main thread
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    scrollToBottom();

                                    value = (String) dataSnapshot.child("users").child(UserDetails.chatWith).child("token").getValue();
                                    sendFCMNotification(value, messageText);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }


                            });

                        }
                    };
                    mainHandler.post(myRunnable);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {

            //Adds the new message to database???
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

 //This sends a push notification to the user using a POST request
    private void sendFCMNotification(String token, String reminderMessage) {

        try {
        //send Push Notification
            HttpsURLConnection connection = null;
            try {

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=" + "AAAAJ6Ftp0Q:APA91bE_zC-a3x7_SobgoxfLeptgBUu5EUw4bCCxYW2SoKVr90cS6rvfSz7UhvmPjRAlXtgElKLXekTFYO9TVJSQLQPBXqHFEZgKsSSNs4yMqiK2ReIMcuzXTgwkp0ZoeZxP5xQF5HKb");

                JSONObject root = new JSONObject();
                JSONObject data = new JSONObject();
                data.put("body", reminderMessage);
                data.put("title", "Message from " + UserDetails.chatWith);
                root.put("notification", data);
                root.put("to",token);

                byte[] outputBytes = root.toString().getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);
                os.flush();
                os.close();
                connection.getInputStream(); //do not remove this line. request will not work without it gg


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addMessageBox(String message, int type){
        scrollToBottom();
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
           // textView.setBackgroundResource(R.drawable.bubble_in);
            textView.setBackgroundColor(Color.LTGRAY);

        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundColor(Color.YELLOW);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);

    }

    //This scrolls to the bottom of the chat page when called
    public void scrollToBottom() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


}