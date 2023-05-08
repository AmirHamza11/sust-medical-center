package com.example.sustmedicalcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfileActivity extends AppCompatActivity {

    private CircleImageView otherUserDisplayImageCIV;
    private TextView otherUserUserNameTV;
    private TextView otherUserEmailTV;
    private TextView otherUserDoctorDetailsTV;
    private TextView otherUserDepartmentTV;
    private TextView otherUserRegistrationNoTV;
    private TextView otherUserPhoneNoTV;
    private TextView otherUserBloodGroupTV;
    private TextView otherUserBloodDonationDateTV;
    private TextView otherUserChronicDiseaseTV;
    private MaterialButton otherUserChatButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        User otherUser = (User) getIntent().getSerializableExtra("otherUser");


        otherUserDisplayImageCIV = findViewById(R.id.other_user_profile_display_image_civ_id);
        otherUserUserNameTV = findViewById(R.id.other_user_profile_user_name_tv_id);
        otherUserEmailTV = findViewById(R.id.other_user_profile_email_tv_id);
        otherUserDoctorDetailsTV = findViewById(R.id.other_user_profile_doctor_details_tv_id);
        otherUserDepartmentTV = findViewById(R.id.other_user_profile_department_tv_id);
        otherUserRegistrationNoTV = findViewById(R.id.other_user_profile_registration_no_tv_id);
        otherUserPhoneNoTV = findViewById(R.id.other_user_profile_phone_no_tv_id);
        otherUserBloodGroupTV = findViewById(R.id.other_user_profile_blood_group_tv_id);
        otherUserBloodDonationDateTV = findViewById(R.id.other_user_profile_last_day_of_blood_donation_tv_id);
        otherUserChronicDiseaseTV = findViewById(R.id.other_user_profile_chronic_disease_tv_id);
        otherUserChatButton = findViewById(R.id.other_user_profile_chat_button_id);


        if(otherUser.getUserType().equals("0")){
            otherUserDoctorDetailsTV.setVisibility(View.GONE);
        }else{
            otherUserDepartmentTV.setVisibility(View.GONE);
            otherUserRegistrationNoTV.setVisibility(View.GONE);
        }

        /*
        using picasso library to download display image with the download url and loading the image
        in circle image view.
         */
        if(!otherUser.getDisplayImageUrl().equals("")){
            Picasso.get().load(otherUser.getDisplayImageUrl()).placeholder(R.drawable.d1).into(otherUserDisplayImageCIV);
        }
        otherUserUserNameTV.setText(otherUser.getUserName());
        otherUserEmailTV.setText("Email: " + otherUser.getEmail());
        otherUserDoctorDetailsTV.setText("Details: " + otherUser.getDoctorDetails());
        otherUserDepartmentTV.setText("Dept: " +otherUser.getDepartment());
        otherUserRegistrationNoTV.setText("Reg No: " + otherUser.getRegistrationNo());
        otherUserPhoneNoTV.setText("Phone No: " +otherUser.getPhoneNo());
        otherUserBloodGroupTV.setText("Blood Group: " +otherUser.getBloodGroup());
        otherUserBloodDonationDateTV.setText("Last day of blood donation: " +otherUser.getLastDayOfBloodDonation());
        otherUserChronicDiseaseTV.setText("Chronic Diseases: " +otherUser.getChronicDisease());



        otherUserChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherUserProfileActivity.this,ChatRoomActivity.class);
                intent.putExtra("UID", otherUser.getUserUid());
                intent.putExtra("DisplayImageUrl", otherUser.getDisplayImageUrl());
                String userType = otherUser.getUserType().equals("0")?" (Student)" : " (Doctor)";
                intent.putExtra("UserName", otherUser.getUserName() + userType);
                startActivity(intent);
            }
        });


    }
}