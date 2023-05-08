package com.example.sustmedicalcenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.controller.NewAppointmentDoctorsAdapter;
import com.example.sustmedicalcenter.controller.NewAppointmentTimesAdapter;
import com.example.sustmedicalcenter.model.Appointment;
import com.example.sustmedicalcenter.model.Day;
import com.example.sustmedicalcenter.model.Doctor;
import com.example.sustmedicalcenter.model.PrescriptionItem;
import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewAppointmentActivity extends AppCompatActivity implements NewAppointmentDoctorsAdapter.ItemOnClickListener, NewAppointmentTimesAdapter.ItemClickListener{

    TextInputEditText problemTitleET, problemDescriptionET;
    RecyclerView doctorItemsRecyclerView, availableTimesRecyclerView;
    ArrayList<User> doctors;
    ArrayList<String> availableTimes;
    ImageButton chooseDoctorButton, chooseDateButton;
    CircleImageView doctorDisplayImage;
    TextView doctorName, doctorDetails, chosenDateTime;
    MaterialButton confirmAppointmentButton;
    NewAppointmentTimesAdapter newAppointmentTimesAdapter;

    FirebaseFirestore db;

    String chosenTime, formattedChosenDate;

    Boolean isChosenTime, isChosenDate, isChosenDoctor;

    ConstraintLayout chosenDoctorDetailsConstraintLayout;

    String chosenDoctorUid,chosenDoctorDisplayImageUrl,chosenDoctorName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        chosenTime = "";
        formattedChosenDate = "";
        chosenDoctorUid = "";
        chosenDoctorName = "";
        isChosenDate = isChosenDoctor = isChosenTime = false;


        problemTitleET = findViewById(R.id.new_appointment_problem_title_editText_id);
        problemDescriptionET = findViewById(R.id.new_appointment_problem_description_editText_id);
        doctorItemsRecyclerView = findViewById(R.id.new_appointment_choose_doctor_recyclerView_id);
        availableTimesRecyclerView = findViewById(R.id.new_appointment_pick_time_recyclerView_id);
        chooseDoctorButton = findViewById(R.id.new_appointment_choose_doctor_image_button_id);
        chooseDateButton = findViewById(R.id.new_appointment_choose_date_image_button_id);
        doctorDisplayImage = findViewById(R.id.new_appointment_doctor_display_image_civ_id);
        doctorName = findViewById(R.id.new_appointment_doctor_name_textView_id);
        doctorDetails = findViewById(R.id.new_appointment_doctor_details_textView_id);
        chosenDateTime = findViewById(R.id.new_appointment_chosen_date_time_textView_id);
        confirmAppointmentButton = findViewById(R.id.new_appointment_confirm_appointment_button_id);
        chosenDoctorDetailsConstraintLayout = findViewById(R.id.new_appointment_doctor_description_constraint_layout_id);
        chosenDoctorDetailsConstraintLayout.setVisibility(View.GONE);

        chosenDateTime.setText("");

        db = FirebaseFirestore.getInstance();

        doctorItemsRecyclerView.setVisibility(View.GONE);
        availableTimesRecyclerView.setVisibility(View.GONE);


        doctors = new ArrayList<>();

        /*
        fetching all the doctors for the user to choose his/her preferred one.
         */
        db.collection("users")
                .whereEqualTo("userType","1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            for(QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                doctors.add(user);
                            }
                        }else {
                            Log.d("NewAppointmentDoctors", task.getException().toString());
                        }
                    }
                });


        doctorItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        doctorItemsRecyclerView.setAdapter(new NewAppointmentDoctorsAdapter(doctors, this, this));



        availableTimes = new ArrayList<>();


        availableTimesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newAppointmentTimesAdapter = new NewAppointmentTimesAdapter(this,availableTimes,this);
        availableTimesRecyclerView.setAdapter(newAppointmentTimesAdapter);


        chooseDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorItemsRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

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

                        String formattedDate = dateFormat.format(selection);
                        chosenDateTime.setText(formattedDate);
                        formattedChosenDate = formattedDate;
                        showTimePickerRecyclerView();
                        isChosenDate = true;

                        availableTimes.clear();

                        /*
                        fetching times which are available in the chosen date to place a new appointment.
                         */
                        db.collection("days")
                                .whereEqualTo("date", formattedDate)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {


                                            for(QueryDocumentSnapshot document : task.getResult()) {
                                                Day day = document.toObject(Day.class);
                                                for(int position = 0; position < day.getIsAvailable().size(); position++) {
                                                    if(day.getIsAvailable().get(position)) {
                                                        availableTimes.add(day.getAvailableTimes().get(position));
                                                    }
                                                }
                                                for(String available:availableTimes){

                                                    newAppointmentTimesAdapter.setTimeItems(availableTimes);
                                                    newAppointmentTimesAdapter.notifyDataSetChanged();
                                                    showTimePickerRecyclerView();
                                                }
                                            }


                                        }else{
                                            Log.d("NewAppointmentAvailableTimes", task.getException().toString());

                                        }
                                    }
                                });





                    }

                });

            }
        });


        confirmAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isChosenDoctor || !isChosenDate || !isChosenTime || problemTitleET.getText().toString().equals("") || problemDescriptionET.getText().toString().equals("")) {
                    Toast.makeText(NewAppointmentActivity.this, "Choose all the fields", Toast.LENGTH_LONG).show();
                    return;
                }

                Long currentTime = System.currentTimeMillis();
                String formattedDate = formattedChosenDate;
                String time = chosenTime;
                String applicantsDisplayImageUrl = CurrentUserSingleton.getInstance().getCurrentUser().getDisplayImageUrl();
                String applicantsName = CurrentUserSingleton.getInstance().getCurrentUser().getUserName();
                String applicantsMissCount = "0";
                String problemTitle = problemTitleET.getText().toString();
                String problemDesc = problemDescriptionET.getText().toString();
                String applicantsUid = CurrentUserSingleton.getInstance().getCurrentUser().getUserUid();
                String doctorsUid = chosenDoctorUid;
                ArrayList<PrescriptionItem> prescriptionList = new ArrayList<>();
                String doctorDisplayImageUrl = chosenDoctorDisplayImageUrl;

                Appointment appointment = new Appointment("",currentTime, formattedDate, time, applicantsDisplayImageUrl, doctorDisplayImageUrl, applicantsName, applicantsMissCount, chosenDoctorName , problemTitle, problemDesc, prescriptionList, applicantsUid, doctorsUid,"0");

                /*
                adding new appointment document in "appointments" collection.
                 */
                db.collection("appointments")
                        .add(appointment)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();


                                db.collection("appointments").document(id)
                                        .update("appointmentUid",id)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                /*
                                                updating the newly created appointments time as unavailable
                                                for further use.
                                                 */
                                                db.collection("days")
                                                        .whereEqualTo("date",formattedDate)
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
                                                                            if(availableTimes.get(pos).equals(chosenTime)) {
                                                                                isAvailable.set(pos,false);
                                                                                break;
                                                                            }
                                                                        }
                                                                        db.collection("days")
                                                                                .document(documentId)
                                                                                .update("isAvailable",isAvailable)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Log.d("NewAppointmentDayUpdateSuccess", "onSuccess: yay ");
                                                                                        finish();
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.d("NewAppointmentDayUpdateFailure",e.toString());
                                                                                    }
                                                                                });
                                                                    }

                                                                }else{
                                                                    Log.d("NewAppointmentGetDayFailure", task.getException().toString());

                                                                }
                                                            }
                                                        });


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("NewAppointmentUpdate", e.toString());
                                            }
                                        });



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("AppointmentAddingFail", e.toString());
                            }
                        });


            }

        });


    }

    private void showTimePickerRecyclerView() {
        availableTimesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDoctorItemClicked(int position) {
        doctorName.setText(doctors.get(position).getUserName());
        doctorDetails.setText(doctors.get(position).getDoctorDetails());
        doctorItemsRecyclerView.setVisibility(View.GONE);
        isChosenDoctor = true;
        chosenDoctorDetailsConstraintLayout.setVisibility(View.VISIBLE);
        chosenDoctorUid = doctors.get(position).getUserUid();
        chosenDoctorDisplayImageUrl = doctors.get(position).getDisplayImageUrl();
        chosenDoctorName = doctors.get(position).getUserName();
    }

    @Override
    public void onTimeItemClicked(int position) {

        String chosenDate = chosenDateTime.getText().toString();
        chosenDateTime.setText(chosenDate + " " + availableTimes.get(position));
        chosenTime = availableTimes.get(position);
        availableTimesRecyclerView.setVisibility(View.GONE);
        isChosenTime = true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}