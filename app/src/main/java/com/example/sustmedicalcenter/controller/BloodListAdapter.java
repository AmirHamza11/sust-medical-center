package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.model.BloodModel;
import com.example.sustmedicalcenter.R;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.BloodModel;

import java.util.ArrayList;


public class BloodListAdapter extends RecyclerView.Adapter<BloodListAdapter.ViewHolder> {
    Context context;
    ArrayList<BloodModel> BloodList;

    public BloodListAdapter(Context context, ArrayList<BloodModel> bloodList) {
        this.context = context;
        BloodList = bloodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_recyclerview_design,null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(BloodList.get(position).getName());
        holder.phone.setText(BloodList.get(position).getPhone_no());
        holder.donation_date.setText(BloodList.get(position).getLast_day_of_donation());
        holder.blood_profile_pic.setImageResource(BloodList.get(position).getProfile_pic());




    }

    @Override
    public int getItemCount() {
        return BloodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone,donation_date;
        ImageView blood_profile_pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.blood_student_name);
            phone = itemView.findViewById(R.id.blood_phone_no);
            donation_date = itemView.findViewById(R.id.blood_last_day_0f_donation);
            blood_profile_pic= itemView.findViewById(R.id.student_profile_imageview);

            blood_profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //Go to Home Fragment
                }
            });



            //pressing the phone no user can call directly//

            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone_no= phone.getText().toString().replaceAll("-", "");
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(phone_no));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(callIntent);
                }
            });
        }
    }
}
