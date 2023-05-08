package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.model.MedicineListModel;
import com.example.sustmedicalcenter.R;

import java.util.ArrayList;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder>{

    Context context;
    ArrayList<MedicineListModel> MedicineList;

    public MedicineListAdapter(Context context, ArrayList<MedicineListModel> medicineList) {
        this.context = context;
        MedicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list,null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineListAdapter.ViewHolder holder, int position) {
        holder.med_name.setText(MedicineList.get(position).getMedicineName());
        holder.generic_name.setText(MedicineList.get(position).getGenericName());

    }

    @Override
    public int getItemCount() {
        return MedicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView med_name,generic_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           med_name = itemView.findViewById(R.id.medicine_name);
           generic_name = itemView.findViewById(R.id.medicine_generic_name);

        }
    }
}
