package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.Message;
import com.example.sustmedicalcenter.view.IncomingMessageViewHolder;
import com.example.sustmedicalcenter.view.OutGoingMessageViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Message> messages;
    private final int OUTGOING_MESSAGE_TYPE = 1;

    public ChatRoomAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }


    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getMessageType();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == OUTGOING_MESSAGE_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.outgoing_message,parent,false);
            return new OutGoingMessageViewHolder(view);

        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.incoming_message,parent,false);
            return new IncomingMessageViewHolder(view);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if(messages.get(position).getMessageType() == OUTGOING_MESSAGE_TYPE){
            ((OutGoingMessageViewHolder)holder).bind(messages.get(position));
        }else{
            ((IncomingMessageViewHolder)holder).bind(messages.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message msg) {
        messages.add(0,msg);
        notifyItemInserted(0);

    }
}
