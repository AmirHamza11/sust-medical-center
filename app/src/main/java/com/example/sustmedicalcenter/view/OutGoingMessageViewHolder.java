package com.example.sustmedicalcenter.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.Message;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OutGoingMessageViewHolder extends RecyclerView.ViewHolder {

    private TextView messageTV;
    private TextView dateTimeTv;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);


    public OutGoingMessageViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        messageTV = itemView.findViewById(R.id.outgoing_message_message_textView_id);
        dateTimeTv = itemView.findViewById(R.id.outgoing_message_date_time_textView_id);

    }

    public void bind(Message message){

        messageTV.setText(message.getMessageText());
        dateTimeTv.setText(formatter.format(message.getSentTimeInMillies()));


    }
}
