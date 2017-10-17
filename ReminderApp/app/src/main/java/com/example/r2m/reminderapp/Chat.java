package com.example.r2m.reminderapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Firebase.setAndroidContext(this);
        reference1 = database.getReference("messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = database.getReference("messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        reference1.getDatabase();



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    sendFCMNotification();
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // Map map = dataSnapshot.getValue(Map.class);
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

    private void sendFCMNotification() {

        try {
            //TODO this FCM post request not working

           // Log.d(TAG, "API CALL: POST "+url);
            Toast.makeText(this, "Enter email plox", Toast.LENGTH_SHORT).show();
            OutputStream output = null;
            String query = "furnail_name=hell&hi=2";//use URLEncode here
            URLConnection connection = new URL("https://fcm.googleapis.com/fcm/send").openConnection();
            //String authString = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(),Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", "AAAAJ6Ftp0Q:APA91bE_zC-a3x7_SobgoxfLeptgBUu5EUw4bCCxYW2SoKVr90cS6rvfSz7UhvmPjRAlXtgElKLXekTFYO9TVJSQLQPBXqHFEZgKsSSNs4yMqiK2ReIMcuzXTgwkp0ZoeZxP5xQF5HKb");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("To", "dtEW9bbkOf8:APA91bGN_oHvsik8x-mmhJOXqrc2k7G9m270j1Db5rqvKHWSzEzNScv5SveyERDiQ57wMnJtjCztTvqZ51VIUTkPjy90524KDDqwT3d-y2HbgtLPdFSu5j4_ydWBrTwi_4d6w_asTfXJ");
            connection.setRequestProperty("notification", "lolol");
            output = connection.getOutputStream();
            output.write(query.getBytes("UTF-8"));
            //InputStream response = connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addMessageBox(String message, int type){
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
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}