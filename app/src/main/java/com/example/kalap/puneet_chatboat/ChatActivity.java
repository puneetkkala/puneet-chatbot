package com.example.kalap.puneet_chatboat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {

    EditText getText;
    LinearLayout display;
    static int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageButton sendMessage= (ImageButton) findViewById(R.id.sendMessage);
        getText = (EditText) findViewById(R.id.getText);
        display = (LinearLayout) findViewById(R.id.display);
        ChatDatabase chatDatabase = new ChatDatabase(getApplicationContext());
        SQLiteDatabase db = chatDatabase.getReadableDatabase();
        String[] projection = {ChatDatabase.MESSAGE,ChatDatabase.MESSAGE_TYPE};
        Cursor cursor = db.query(
                ChatDatabase.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                ChatDatabase.MESSAGE_ID
                );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String type = cursor.getString(cursor.getColumnIndex(ChatDatabase.MESSAGE_TYPE));
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabase.MESSAGE));
            TextView tvS = new TextView(getApplicationContext());
            Drawable sent = getResources().getDrawable(R.drawable.bubble_a);
            final Drawable receive = getResources().getDrawable(R.drawable.bubble_b);
            if(type.equals("sent")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvS.setBackground(sent);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tvS.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                }
            }
            if(type.equals("received")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvS.setBackground(receive);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tvS.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
            }
            tvS.setText(message);
            tvS.setTextSize(20);
            display.addView(tvS);
            cursor.moveToNext();
        }
        sendMessage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                sendMessage(view);
            }
        });

    }

    public void sendMessage(View view){

        ChatDatabase chatDatabase = new ChatDatabase(getApplicationContext());
        SQLiteDatabase db = chatDatabase.getWritableDatabase();

        RequestQueue queue = Volley.newRequestQueue(this);

        final String message = getText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(ChatDatabase.MESSAGE_ID,num++);
        values.put(ChatDatabase.MESSAGE,message);
        values.put(ChatDatabase.MESSAGE_TYPE,"sent");
        db.insert(ChatDatabase.TABLE_NAME,null,values);

        Drawable sent = getResources().getDrawable(R.drawable.bubble_a);
        final Drawable receive = getResources().getDrawable(R.drawable.bubble_b);
        TextView tvS = new TextView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tvS.setBackground(sent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tvS.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        tvS.setText(message);
        tvS.setTextSize(20);
        display.addView(tvS);

        String url = "http://www.personalityforge.com/api/chat/?apiKey=6nt5d1nJHkqbkphe&chatBotID=63906" +
                "&message="+ message +"&externalID=chirag1";

        final TextView tvR = new TextView(this);
        tvR.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tvR.setBackground(receive);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tvR.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ChatDatabase chatDatabase = new ChatDatabase(getApplicationContext());
                        SQLiteDatabase db = chatDatabase.getWritableDatabase();
                        String message = "";
                        JSONObject jsonObject;
                        try {
                            jsonObject = response.getJSONObject("message");
                            message = jsonObject.getString("message");
                        } catch (JSONException e) {
                            Log.i("BOTREPLY",e.getMessage());
                        }
                        ContentValues values = new ContentValues();
                        values.put(ChatDatabase.MESSAGE_ID,num++);
                        values.put(ChatDatabase.MESSAGE,message);
                        values.put(ChatDatabase.MESSAGE_TYPE,"received");
                        db.insert(ChatDatabase.TABLE_NAME,null,values);
                        tvR.setText(message);
                        display.addView(tvR);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("BOTREPLY", error.getMessage());
                    }
                }
        );
        queue.add(jsonObjectRequest);
        getText.setText("");
    }
}
