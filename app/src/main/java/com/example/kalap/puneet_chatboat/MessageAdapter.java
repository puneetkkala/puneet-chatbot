package com.example.kalap.puneet_chatboat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<BotAppMessage> messageList;
    private Context context;

    public MessageAdapter(List<BotAppMessage> messageList, Context context){
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        BotAppMessage bam = messageList.get(position);
        holder.vMessage.setText(bam.getContent());
        if(bam.getType().equals("R")){
//            holder.vMessage.setBackgroundColor(context.getResources()
//                .getColor(android.R.color.holo_blue_dark));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.vMessage.setBackground(context.getResources()
                        .getDrawable(R.mipmap.bubble_yellow));
            }
        } else if(bam.getType().equals("S")){
//            holder.vMessage.setBackgroundColor(context.getResources()
//                    .getColor(android.R.color.holo_green_dark));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.vMessage.setBackground(context.getResources()
                        .getDrawable(R.mipmap.bubble_green));
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}