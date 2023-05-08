package com.example.sustmedicalcenter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sustmedicalcenter.controller.DoctorAppointmentAdapter;
import com.example.sustmedicalcenter.model.Appointment;
import com.example.sustmedicalcenter.model.Day;
import com.example.sustmedicalcenter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DoctorAllScheduleActivity extends AppCompatActivity implements DoctorAppointmentAdapter.itemOnClickListener {

    TextView appbarDate;
    MaterialButton chooseDateButton;
    RecyclerView allScheduleRecyclerView;
    DoctorAppointmentAdapter appointmentAdapter;
    ArrayList<Appointment> appointments;
    SimpleDateFormat dateFormat;
    FirebaseFirestore db;

    String doctorUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_all_schedule);

        db = FirebaseFirestore.getInstance();

        doctorUid = getIntent().getStringExtra("DoctorUid");


        appointments = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);


        appbarDate = findViewById(R.id.doctor_all_schedule_date_textView_id);
        chooseDateButton = findViewById(R.id.doctor_all_schedule_choose_date_button_id);
        allScheduleRecyclerView = findViewById(R.id.doctor_all_schedule_recyclerView_id);
        appointmentAdapter = new DoctorAppointmentAdapter(appointments, this,this);
        allScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allScheduleRecyclerView.setAdapter(appointmentAdapter);

        updateAppointsList(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,29);
        long nextMonthDateInMillies = calendar.getTimeInMillis();


        CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator minDateValidator = DateValidatorPointForward.from(MaterialDatePicker.todayInUtcMilliseconds());
        CalendarConstraints.DateValidator maxDateValidator = DateValidatorPointBackward.before(nextMonthDateInMillies);

        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
        listValidators.add(minDateValidator);
        listValidators.add(maxDateValidator);
        CalendarConstraints.DateValidator compositeDateValidator = CompositeDateValidator.allOf(listValidators);
        calendarConstraints.setValidator(compositeDateValidator);



        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder mDAtePickerBuilder =  MaterialDatePicker.Builder.datePicker();
                mDAtePickerBuilder.setTitleText("Choose a date");
                mDAtePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                mDAtePickerBuilder.setCalendarConstraints(calendarConstraints.build());
                MaterialDatePicker materialDatePicker = mDAtePickerBuilder.build();


                materialDatePicker.show(getSupportFragmentManager(),"");

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {

                        if(selection == MaterialDatePicker.todayInUtcMilliseconds()){
                            appbarDate.setText("Today");
                        }else{
                            String formattedDate = dateFormat.format(selection);
                            appbarDate.setText(formattedDate);
                        }

                        updateAppointsList(selection);

                    }

                });
            }
        });



    }

    /**
     * method to fetch appointments which are placed in the chosen date.
     */
    private void updateAppointsList(Long chosenDateinMilies) {

        String formattedDate = dateFormat.format(chosenDateinMilies);

        appointments.clear();

        /*
        fetching the requested day's appointments.
         */
        db.collection("appointments")
                .whereEqualTo("formattedDate", formattedDate)
                .whereEqualTo("doctorUid",doctorUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                Appointment appointment = document.toObject(Appointment.class);
                                appointments.add(appointment);

                            }
                            appointmentAdapter.setAppointments(appointments);
                            appointmentAdapter.notifyDataSetChanged();
                        }else {
                            Log.d("doctorAllScheduleAppointments", task.getException().toString());
                        }

                    }
                });


    }

    @Override
    public void onSuspendButtonClicked(int position) {

        new MaterialAlertDialogBuilder(this)
                .setTitle("Suspend this Appointment")
                .setMessage("Are you sure you want to suspend this appointment?")
                .setPositiveButton("Suspend", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Appointment appointment = appointments.get(position);
                        String appointmentDate = appointment.getFormattedDate();
                        String appointmentTime = appointment.getTime();


                        /*
                        fetching and updating the suspended appointment's time as available for placing new
                        appointments.
                         */
                        db.collection("days")
                                .whereEqualTo("date",appointmentDate)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                           for(QueryDocumentSnapshot document: task.getResult()) {
                                               String documentId = document.getId();
                                               Day day = document.toObject(Day.class);
                                               ArrayList<String> availableTimes = day.getAvailableTimes();
                                               ArrayList<Boolean> isAvailable = day.getIsAvailable();
                                               for(int pos=0; pos < availableTimes.size(); pos++) {
                                                   if(availableTimes.get(pos).equals(appointmentTime)) {
                                                       isAvailable.set(pos,true);
                                                       break;
                                                   }
                                               }

                                               db.collection("days")
                                                       .document(documentId)
                                                       .update("isAvailable",isAvailable)
                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void unused) {
                                                               Log.d("DoctorAllScheduleSuspendUpdateSuccess", "onSuccess: yay ");
                                                           }
                                                       })
                                                       .addOnFailureListener(new OnFailureListener() {
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {
                                                               Log.d("DoctorAllScheduleSuspendUpdateFailure",e.toString());
                                                           }
                                                       });
                                           }

                                        }else{
                                            Log.d("DoctorAllScheduleGetDayFailure", task.getException().toString());

                                        }
                                    }
                                });


                        db.collection("appointments")
                                .document(appointment.getAppointmentUid())
                                .update("appointmentStatus", "2")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        appointmentAdapter.getAppointments().remove(position);
                                        appointmentAdapter.notifyItemRemoved(position);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("DoctorAllScheduleDelete", e.toString());
                                    }
                                });


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

    }

    @Override
    public void onOpeningUserProfile(int position) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        /*
        fetching the chosen user's details to show it in OtherUserProfileActivity.
         */
        db.collection("users").document(appointments.get(position).getApplicantUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            Intent intent = new Intent(DoctorAllScheduleActivity.this,OtherUserProfileActivity.class);
                            intent.putExtra("otherUser",user);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }else{
                            Log.d("salauff", task.getException().getMessage());
                            progressDialog.dismiss();
                        }

                    }
                });


    }

    @Override
    public void onChatButtonClicked(int position) {
        Intent intent = new Intent(DoctorAllScheduleActivity.this,ChatRoomActivity.class);
        intent.putExtra("UID",appointments.get(position).getApplicantUid());
        intent.putExtra("DisplayImageUrl",appointments.get(position).getApplicantsDisplayImageUrl());
        intent.putExtra("UserName",appointments.get(position).getApplicantsName());
        startActivity(intent);

    }
}
