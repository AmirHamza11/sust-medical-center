package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.view.DoctorViewHolder;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorViewHolder> {

    private Context context;
    private ArrayList<User>doctors;
    private ItemOnClickListener itemOnClickListener;

    public DoctorListAdapter(Context context, ArrayList<User> doctors, ItemOnClickListener itemOnClickListener) {
        this.context = context;
        this.doctors = doctors;
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_list_doctor,parent,false);
        return new DoctorViewHolder(view,itemOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        holder.bind(doctors.get(position));


    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public void setDoctors(ArrayList<User> doctors){
        this.doctors = doctors;
    }


}
