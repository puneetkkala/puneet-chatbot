package com.example.kalap.puneet_chatboat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageButton sendMessage= (ImageButton) findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                sendMessage(view);
            }
        });
    }

    public void sendMessage(View view){
        Log.i("BOTREPLY","Inside sendMessage");
        RequestQueue queue = Volley.newRequestQueue(this);
        //String message = "Hi";
        //String url = "http://www.personalityforge.com/api/chat/?apiKey=6nt5d1nJHkqbkphe&chatBotID=63906" +
        //        "&message="+ message +"&externalID=chirag1";

        String url = "http://www.personalityforge.com/api/chat/?apiKey=6nt5d1nJHkqbkphe&message=Hi&chatBotID=63906&externalID=chirag1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("BOTREPLY", response.toString());
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
    }
}
