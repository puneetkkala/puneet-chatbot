package com.example.kalap.puneet_chatboat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private List<BotAppMessage> messageList;
    private MessageAdapter messageAdapter;
    EditText getText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageButton sendMessage = (ImageButton) findViewById(R.id.sendMessage);
        getText = (EditText) findViewById(R.id.getText);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, getApplicationContext());

        RecyclerView cardList = (RecyclerView) findViewById(R.id.cardList);
        cardList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setLayoutManager(llm);
        cardList.setAdapter(messageAdapter);

        sendMessage.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });

    }

    public void sendMessage(View view) {

//        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        RequestQueue queue = Volley.newRequestQueue(this);
        BotAppMessage appMessage = new BotAppMessage(getText.getText().toString(),"S");
        messageList.add(appMessage);
        messageAdapter.notifyDataSetChanged();
        String url = "http://www.personalityforge.com/api/chat/?apiKey=6nt5d1nJHkqbkphe&chatBotID=63906" +
                        "&message=" + appMessage.getContent() + "&externalID=chirag1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BotAppMessage appMessage1;
                    JSONObject jsonObject;
                    try {
                        jsonObject = response.getJSONObject("message");
                        appMessage1 = new BotAppMessage(jsonObject.getString("message"),"R");
                        messageList.add(appMessage1);
                        messageAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.i("BOTREPLY", e.getMessage());
                    }
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