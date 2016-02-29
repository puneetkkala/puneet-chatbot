package com.example.kalap.puneet_chatboat;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private List<BotAppMessage> messageList;
    private MessageAdapter messageAdapter;
    private SharedPreferences preferences;
    EditText getText;
    private BotMessageDbHelper messageDbHelper;
    private SQLiteDatabase database;
    private Set<String> offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageButton sendMessage = (ImageButton) findViewById(R.id.sendMessage);
        getText = (EditText) findViewById(R.id.getText);

        messageList = new ArrayList<>();
        populateList();
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
                if(isConnected()){
                    Snackbar snackbar = Snackbar.make(view,"Processing offline messages",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    processOffline();
                    sendMessage();
                } else {
                    Snackbar snackbar = Snackbar.make(view,"You are offline",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    storeOffline();
                }
            }
        });

    }

    public void populateList(){
        messageDbHelper = new BotMessageDbHelper(getApplicationContext());
        database = messageDbHelper.getReadableDatabase();

        String[] projection = {
                BotMessageDbHelper.COLUMN_TYPE,
                BotMessageDbHelper.COLUMN_CONTENT
        };

        Cursor c = database.query(
                BotMessageDbHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToFirst();
        while (!c.isAfterLast()){
            BotAppMessage bam = new BotAppMessage(
                    c.getString(c.getColumnIndex(BotMessageDbHelper.COLUMN_CONTENT)),
                    c.getString(c.getColumnIndex(BotMessageDbHelper.COLUMN_TYPE))
            );
            messageList.add(bam);
            c.moveToNext();
        }
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void addToDatabase(BotAppMessage bam){
        database = messageDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BotMessageDbHelper.COLUMN_TYPE, bam.getType());
        values.put(BotMessageDbHelper.COLUMN_CONTENT, bam.getContent());

        database.insert(BotMessageDbHelper.TABLE_NAME,null, values);
    }

    public void sendMessage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        BotAppMessage appMessage = new BotAppMessage(getText.getText().toString(),"S");
        messageList.add(appMessage);
        messageAdapter.notifyDataSetChanged();
        addToDatabase(appMessage);
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
                        addToDatabase(appMessage1);
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

    public void storeOffline(){
        preferences = getSharedPreferences("offline", MODE_PRIVATE);
        offline = preferences.getStringSet("offline", new HashSet<String>());
        offline.add(getText.getText().toString());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("offline", offline);
        editor.commit();
        BotAppMessage appMessage = new BotAppMessage(getText.getText().toString(),"O");
        messageList.add(appMessage);
        messageAdapter.notifyDataSetChanged();
        getText.setText("");
    }

    public void processOffline(){
        preferences = getSharedPreferences("offline", MODE_PRIVATE);
        offline = preferences.getStringSet("offline", new HashSet<String>());
        RequestQueue queue = Volley.newRequestQueue(this);
        Iterator<String> iterator = offline.iterator();
        while(!iterator.hasNext() && isConnected()){
            String content = iterator.next();
            BotAppMessage appMessage = new BotAppMessage(content,"S");
            messageList.add(appMessage);
            messageAdapter.notifyDataSetChanged();
            addToDatabase(appMessage);
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
                                addToDatabase(appMessage1);
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
            offline.remove(content);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("offline", offline);
        editor.commit();
    }
}