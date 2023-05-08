package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;

import java.util.ArrayList;


public class NewAppointmentTimesAdapter extends RecyclerView.Adapter<NewAppointmentTimesAdapter.ViewHolder>{

    private Context ctx;
    private ArrayList<String> timeItems;
    private NewAppointmentTimesAdapter.ItemClickListener itemClickListener;

    public NewAppointmentTimesAdapter(Context ctx, ArrayList<String> timeItems, NewAppointmentTimesAdapter.ItemClickListener itemOnClickListener) {
        this.ctx = ctx;
        this.timeItems = timeItems;
        this.itemClickListener = itemOnClickListener;
    }

    public interface ItemClickListener {
        void onTimeItemClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.new_appointment_time_item, parent,false);
        return new ViewHolder(v, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.timeTextView.setText(timeItems.get(position));

    }

    @Override
    public int getItemCount() {
        return timeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView timeTextView;
        private NewAppointmentTimesAdapter.ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView, NewAppointmentTimesAdapter.ItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            timeTextView = itemView.findViewById(R.id.new_appointment_time_item_time_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onTimeItemClicked(getBindingAdapterPosition());
                }
            });
        }
    }

    public void setTimeItems(ArrayList<String> timeItems) {
        this.timeItems = timeItems;
    }
}
