package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.Doctor;
import com.example.sustmedicalcenter.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewAppointmentDoctorsAdapter extends RecyclerView.Adapter<NewAppointmentDoctorsAdapter.ViewHolder> {
    ArrayList<User> doctors;
    Context context;
    ItemOnClickListener itemOnClickListener;


    public NewAppointmentDoctorsAdapter(ArrayList<User> doctors, Context context, ItemOnClickListener itemOnClickListener) {
        this.doctors = doctors;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener {
        void onDoctorItemClicked(int position);
    }


    @NonNull

    @Override

    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.new_appointment_doctor_item,parent, false);

        return new ViewHolder(view, itemOnClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {

        if(!doctors.get(position).getDisplayImageUrl().equals("")){
            Picasso.get().load(doctors.get(position).getDisplayImageUrl()).placeholder(R.drawable.d1).into(holder.doctorItemDisplayCIV);
        }
        holder.doctorItemName.setText(doctors.get(position).getUserName());
        holder.doctorItemDescription.setText(doctors.get(position).getDoctorDetails());

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView doctorItemDisplayCIV;
        private TextView doctorItemName, doctorItemDescription;
        private NewAppointmentDoctorsAdapter.ItemOnClickListener itemOnClickListener;

        public ViewHolder(@NonNull  View itemView, NewAppointmentDoctorsAdapter.ItemOnClickListener itemOnClickListener) {
            super(itemView);
            this.itemOnClickListener = itemOnClickListener;
            doctorItemDisplayCIV = itemView.findViewById(R.id.new_appointment_doctor_item_civ_id);
            doctorItemName = itemView.findViewById(R.id.new_appointment_doctor_item_name_id);
            doctorItemDescription = itemView.findViewById(R.id.new_appointment_doctor_item_description_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onDoctorItemClicked(getBindingAdapterPosition());
                }
            });

        }
    }
}
