package com.example.sustmedicalcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustmedicalcenter.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountRequestsFullAccountDetailsActivity extends AppCompatActivity {

    private TextView accountTypeTV, userNameTv, userEmailTV, doctorDetailsTV, userDeptTV, userRegNoTV,
            userPhoneNoTV, userBloodGroupTV, userLastDayOfBloodDonationTV, userChronicDiseaseTV;

    private MaterialButton acceptRequestButton, declineRequestButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_requests_full_account_details);


        User requestedUser = (User) getIntent().getSerializableExtra("requestedUser");


        db = FirebaseFirestore.getInstance();

        accountTypeTV = findViewById(R.id.account_request_full_details_account_type_tV_id);
        userNameTv = findViewById(R.id.account_request_full_details_user_name_tV_id);
        userEmailTV = findViewById(R.id.account_request_full_details_user_email_TV_id);
        doctorDetailsTV = findViewById(R.id.account_request_full_details_doctor_details_tV_id);
        userDeptTV = findViewById(R.id.account_request_full_details_department_tV_id);
        userRegNoTV = findViewById(R.id.account_request_full_details_registration_tV_id);
        userPhoneNoTV = findViewById(R.id.account_request_full_details_phone_no_tV_id);
        userBloodGroupTV = findViewById(R.id.account_request_full_details_blood_group_tV_id);
        userLastDayOfBloodDonationTV = findViewById(R.id.account_request_full_details_blood_donation_date_tV_id);
        userChronicDiseaseTV = findViewById(R.id.account_request_full_details_chronic_disease_tV_id);
        acceptRequestButton = findViewById(R.id.account_request_full_details_accept_button_id);
        declineRequestButton = findViewById(R.id.account_request_full_details_decline_button_id);

        if(requestedUser.getUserType().equals("0")){
            accountTypeTV.setText("Student Account");
            doctorDetailsTV.setVisibility(View.GONE);
        }
        else {
            accountTypeTV.setText("Doctor Account");
            userRegNoTV.setVisibility(View.GONE);
            userDeptTV.setVisibility(View.GONE);
        }

        userNameTv.setText(requestedUser.getUserName());
        userEmailTV.setText(requestedUser.getEmail());
        doctorDetailsTV.setText(requestedUser.getDoctorDetails());
        userDeptTV.setText(requestedUser.getDepartment());
        userRegNoTV.setText(requestedUser.getRegistrationNo());
        userPhoneNoTV.setText(requestedUser.getPhoneNo());
        userBloodGroupTV.setText(requestedUser.getBloodGroup());
        userLastDayOfBloodDonationTV.setText(requestedUser.getLastDayOfBloodDonation());
        userChronicDiseaseTV.setText(requestedUser.getChronicDisease());


        /*
        accepting the account request and updating data in firebase firestore document.
         */
        acceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users")
                        .document(requestedUser.getUserUid())
                        .update("accountStatus","1")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(),"Successful",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(),"failed",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

            }
        });


        /*
        declining the account request and updating data in firebase firestore document.
         */
        declineRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("users")
                        .document(requestedUser.getUserUid())
                        .update("accountStatus","2")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(),"Successful",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(),"failed",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

            }
        });


    }

}