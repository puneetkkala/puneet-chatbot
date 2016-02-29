package com.example.kalap.puneet_chatboat;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    protected TextView vMessage;

    public MessageViewHolder(View v){
        super(v);
        vMessage = (TextView) v.findViewById(R.id.textMessage);
    }
}
