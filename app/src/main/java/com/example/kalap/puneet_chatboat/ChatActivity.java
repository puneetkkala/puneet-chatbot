package com.example.kalap.puneet_chatboat;

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
    static int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageButton sendMessage= (ImageButton) findViewById(R.id.sendMessage);
        getText = (EditText) findViewById(R.id.getText);
        display = (LinearLayout) findViewById(R.id.display);
        sendMessage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                sendMessage(view);
            }
        });
    }

    public void sendMessage(View view){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String message = getText.getText().toString();
        TextView tvS = new TextView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tvS.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        tvS.setText(message);
        display.addView(tvS);
        String url = "http://www.personalityforge.com/api/chat/?apiKey=6nt5d1nJHkqbkphe&chatBotID=63906" +
                "&message="+ message +"&externalID=chirag1";

        final TextView tvR = new TextView(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = "";
                        JSONObject jsonObject;
                        try {
                            jsonObject = response.getJSONObject("message");
                            message = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
