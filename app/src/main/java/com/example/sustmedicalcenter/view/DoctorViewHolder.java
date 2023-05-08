package com.example.sustmedicalcenter.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.controller.DoctorListAdapter;
import com.example.sustmedicalcenter.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView doctorDisplayImageCIV;
    TextView doctorNameTV;
    TextView doctorAppointmentCountTV;
    DoctorListAdapter.ItemOnClickListener itemOnClickListener;


    public DoctorViewHolder(@NonNull View itemView, DoctorListAdapter.ItemOnClickListener itemOnClickListener) {
        super(itemView);

        doctorDisplayImageCIV = itemView.findViewById(R.id.doctor_display_image_id);
        doctorNameTV = itemView.findViewById(R.id.doctor_name_id);
        doctorAppointmentCountTV = itemView.findViewById(R.id.doctor_appointment_count_id);
        this.itemOnClickListener = itemOnClickListener;

        itemView.setOnClickListener(this);

    }

    public void bind(User doctor){
        if(!doctor.getDisplayImageUrl().equals("")){
            Picasso.get().load(doctor.getDisplayImageUrl()).placeholder(R.drawable.d1).into(doctorDisplayImageCIV);
        }
        doctorNameTV.setText(doctor.getUserName());
        doctorAppointmentCountTV.setText("Appointments: "+ doctor.getDoctorAppointmentCount());

    }

    @Override
    public void onClick(View view) {
        itemOnClickListener.onItemClick(getBindingAdapterPosition());
    }
}
