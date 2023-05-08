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

public class IncomingMessageViewHolder extends RecyclerView.ViewHolder {

    private TextView messageTV;
    private TextView dateTimeTV;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);

    public IncomingMessageViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        messageTV = itemView.findViewById(R.id.incoming_message_message_textView_id);
        dateTimeTV = itemView.findViewById(R.id.incoming_message_date_time_textView_id);

    }

    public void bind(Message message){

        messageTV.setText(message.getMessageText());
        dateTimeTV.setText(formatter.format(message.getSentTimeInMillies()));

    }
}
