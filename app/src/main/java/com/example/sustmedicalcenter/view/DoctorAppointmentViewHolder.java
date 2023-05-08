package com.example.sustmedicalcenter.view;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.controller.DoctorAppointmentAdapter;
import com.example.sustmedicalcenter.model.Appointment;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAppointmentViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView applicantsDisplayImageCIV;
    private TextView applicantsUserNameTV;
    public TextView applicantsMissCountTV;
    private TextView problemTitleTV;
    private TextView problemDetailsTV;
    private TextView applicationTime;
    private MaterialButton suspendButton;
    public ConstraintLayout secondConstraintLayout;



    public DoctorAppointmentViewHolder(@NonNull View itemView, DoctorAppointmentAdapter.itemOnClickListener itemOnClickListener, DoctorAppointmentAdapter adapter) {
        super(itemView);

        applicantsDisplayImageCIV = itemView.findViewById(R.id.doctor_single_appointment_displayImage_CIV_id);
        applicantsUserNameTV = itemView.findViewById(R.id.doctor_single_appointment_name_textView_id);
        applicantsMissCountTV = itemView.findViewById(R.id.doctor_single_appointment_user_missCount_textView_id);
        problemTitleTV = itemView.findViewById(R.id.doctor_single_appointment_problem_title_textView_id);
        problemDetailsTV = itemView.findViewById(R.id.doctor_single_appointment_problem_details_textView_id);
        applicationTime = itemView.findViewById(R.id.doctor_single_appointment_time_textView_id);
        suspendButton = itemView.findViewById(R.id.doctor_single_appointment_suspend_button_id);
        secondConstraintLayout = itemView.findViewById(R.id.doctor_single_appointment_second_constraintLayout_id);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appointment appointment = adapter.getAppointments().get(getBindingAdapterPosition());
                appointment.setExpanded(!appointment.isExpanded());
                adapter.notifyItemChanged(getBindingAdapterPosition());

            }
        });

        suspendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onSuspendButtonClicked(getBindingAdapterPosition());
            }
        });
        
    }

    public void bind(Appointment appointment) {

        if(!appointment.getApplicantsDisplayImageUrl().equals("")){
            Picasso.get().load(appointment.getApplicantsDisplayImageUrl()).placeholder(R.drawable.d1).into(applicantsDisplayImageCIV);
        }
        applicantsUserNameTV.setText(appointment.getApplicantsName());
        applicantsMissCountTV.setText(appointment.getApplicantsMissCount());
        problemTitleTV.setText(appointment.getProblemTitle());
        problemDetailsTV.setText(appointment.getProblemDetails());
        applicationTime.setText(appointment.getTime());

    }

    public void expand() {
        applicantsDisplayImageCIV.setVisibility(View.VISIBLE);
        applicantsMissCountTV.setVisibility(View.VISIBLE);
        secondConstraintLayout.setVisibility(View.VISIBLE);

        applicantsUserNameTV.setPadding(10,0,0,0);
        applicantsMissCountTV.setPadding(10,0,0,0);

    }

    public void collapse() {
        applicantsDisplayImageCIV.setVisibility(View.GONE);
        applicantsMissCountTV.setVisibility(View.GONE);
        secondConstraintLayout.setVisibility(View.GONE);
    }
}
