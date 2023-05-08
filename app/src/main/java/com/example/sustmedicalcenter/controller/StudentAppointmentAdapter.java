package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sustmedicalcenter.model.Appointment;
import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAppointmentAdapter extends RecyclerView.Adapter<StudentAppointmentAdapter.ViewHolder> {

    private ArrayList<Appointment> list;
    Context context;
    StudentAppointmentAdapter.itemOnClickListener itemOnClickListener;
    private int listType;

    public interface itemOnClickListener{
        void onCheckupCompletedClicked(int position, int listType);
        void onPrescriptionButtonClicked(int position, int listType);
        void onChatButtonClicked(int position, int listType);
        void onOpeningUserProfile(int position, int listType);

    }


    public StudentAppointmentAdapter(ArrayList<Appointment> list, Context context, StudentAppointmentAdapter.itemOnClickListener itemOnClickListener, int listType){
        this.list=list;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
        this.listType = listType;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_appointment_list_item,parent, false);
        return new ViewHolder (view, itemOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(list.get(position));

        boolean isExpanded = list.get(position).isExpanded();
        holder.student_appointment_second_constraintLayout_id.setVisibility(isExpanded? View.VISIBLE : View.GONE);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addDueItem(Appointment appointment){
        list.add(appointment);
        notifyItemInserted(getItemCount()-1);
    }

    public void addPastItem(Appointment appointment){
        int i = 0;
        for(; i < list.size(); i++){
            if(appointment.getDate() >= list.get(i).getDate()){
                list.add(i,appointment);
                notifyItemInserted(i);
                return;
            }
        }
        list.add(i,appointment);
        notifyItemInserted(i);
    }

    public void removeItem(Appointment appointment){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getAppointmentUid().equals(appointment.getAppointmentUid())){
                list.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView date_tv,time_tv,name_tv,problem_tv,problemdescription_tv;
        private CircleImageView displayImage;
        ConstraintLayout student_appointment_second_constraintLayout_id;
        private ImageButton prescriptionButton;
        private MaterialButton chat,checkupCompleted;



        public ViewHolder(View itemView, StudentAppointmentAdapter.itemOnClickListener itemOnClickListener){
            super(itemView);

            displayImage = itemView.findViewById(R.id.student_appointment_list_item_civ_id);
            date_tv=itemView.findViewById(R.id.student_appointment_date_textView_id);
            time_tv=itemView.findViewById(R.id.student_appointment_list_item_appointment_time_textView_id);
            name_tv=itemView.findViewById(R.id.student_appointment_list_item_doctor_name_textView_id);
            problem_tv=itemView.findViewById(R.id.student_appointment_problem_title_textView_id);
            problemdescription_tv=itemView.findViewById(R.id.student_appointment_problem_details_textView_id);
            student_appointment_second_constraintLayout_id=itemView.findViewById(R.id.student_appointment_second_constraintLayout_id);
            prescriptionButton = itemView.findViewById(R.id.student_appointment_list_item_show_prescription_button_id);
            chat=itemView.findViewById(R.id.student_appointment_list_chat_button);
            checkupCompleted=itemView.findViewById(R.id.student_appointment_completed_button);

            if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("1") && listType == 0){
                checkupCompleted.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Appointment appointment = list.get(getBindingAdapterPosition());
                    appointment.setExpanded(!appointment.isExpanded());
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });



            prescriptionButton.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   itemOnClickListener.onPrescriptionButtonClicked(getBindingAdapterPosition(), listType);
                }
           });


            checkupCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("End This Appointment")
                            .setMessage("Would you like to end this appointment?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkupCompleted.setVisibility(View.GONE);
                                    itemOnClickListener.onCheckupCompletedClicked(getBindingAdapterPosition(),listType);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();

                }
            });

            displayImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onOpeningUserProfile(getBindingAdapterPosition(),listType);
                }
            });

            name_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onOpeningUserProfile(getBindingAdapterPosition(),listType);
                }
            });

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onChatButtonClicked(getBindingAdapterPosition(),listType);
                }
            });



        }

        

        public void bind(Appointment appointment) {
            if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0")){
                name_tv.setText(appointment.getDoctorsName());
                if(!appointment.getDoctorDisplayImageUrl().equals("")){
                    Picasso.get().load(appointment.getDoctorDisplayImageUrl()).placeholder(R.drawable.d1).into(displayImage);
                }
            }else{
                name_tv.setText(appointment.getApplicantsName());
                if(!appointment.getApplicantsDisplayImageUrl().equals("")){
                    Picasso.get().load(appointment.getApplicantsDisplayImageUrl()).placeholder(R.drawable.d1).into(displayImage);
                }
            }


            date_tv.setText(appointment.getFormattedDate());
            time_tv.setText(appointment.getTime());
            problem_tv.setText(appointment.getProblemTitle());
            problemdescription_tv.setText(appointment.getProblemDetails());
        }
    }


}
