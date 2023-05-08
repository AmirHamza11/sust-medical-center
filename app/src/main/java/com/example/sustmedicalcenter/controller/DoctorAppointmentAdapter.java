package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.Appointment;
import com.example.sustmedicalcenter.view.DoctorAppointmentViewHolder;

import java.util.ArrayList;


public class
DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentViewHolder> {

    ArrayList<Appointment>appointments;
    Context context;
    DoctorAppointmentAdapter.itemOnClickListener itemOnClickListener;

    public interface itemOnClickListener{
        void onSuspendButtonClicked(int position);
        void onOpeningUserProfile(int position);
        void onChatButtonClicked(int position);
    }

    public DoctorAppointmentAdapter(ArrayList<Appointment> appointments, Context context, DoctorAppointmentAdapter.itemOnClickListener itemOnClickListener) {
        this.appointments = appointments;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public DoctorAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_single_appointment, parent, false);
        return new DoctorAppointmentViewHolder(view, itemOnClickListener, this);

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentViewHolder holder, int position) {
        holder.bind(appointments.get(position));

        boolean isExpanded = appointments.get(position).isExpanded();
        if (isExpanded) {
            holder.expand();
        } else {
            holder.collapse();
        }

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setAppointments(ArrayList<Appointment>appointments) {
        this.appointments = appointments;

    }

    public ArrayList<Appointment> getAppointments(){
        return this.appointments;
    }
}
