package com.example.sustmedicalcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitialAccountDetails extends AppCompatActivity {

    private MaterialButton chooseStudentTypeButton, chooseDoctorTypeButton, sendAccountRequestButton;
    private TextInputLayout doctorDetailsTIL, registrationNoTIL, departmentTIL, emailTIL, phoneNoTIL, bloodGroupTIL, userNameTIL;
    private TextInputEditText doctorDetailsET, registrationNoET, departmentET, emailET, phoneNoET, bloodGroupET, userNameET;
    private CircleImageView displayImageCIV, chooseDisplayImageCIV;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userUid;

    private static final int STUDENT = 1;
    private static final int DOCTOR = 2;

    private int modeType = STUDENT;

    private Uri imageUri;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_account_details);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        imageUri = null;

        userUid = getIntent().getStringExtra("userUid");

        chooseStudentTypeButton = findViewById(R.id.initial_account_details_choose_student_type_button_id);
        chooseDoctorTypeButton = findViewById(R.id.initial_account_details_choose_doctor_type_button_id);
        sendAccountRequestButton = findViewById(R.id.initial_account_details_send_request_button_id);
        doctorDetailsTIL = findViewById(R.id.initial_account_details_doctor_details_tIL_id);
        doctorDetailsET = findViewById(R.id.initial_account_details_doctor_details_tIET_id);
        registrationNoTIL = findViewById(R.id.initial_account_details_registration_no_tIL_id);
        registrationNoET = findViewById(R.id.initial_account_details_registration_no_tIET_id);
        departmentTIL = findViewById(R.id.initial_account_details_department_tIL_id);
        departmentET = findViewById(R.id.initial_account_details_department_tIET_id);
        phoneNoTIL = findViewById(R.id.initial_account_details_phone_no_tIL_id);
        phoneNoET = findViewById(R.id.initial_account_details_phone_no_tIET_id);
        bloodGroupTIL = findViewById(R.id.initial_account_details_blood_group_tIL_id);
        bloodGroupET = findViewById(R.id.initial_account_details_blood_group_tIET_id);
        displayImageCIV = findViewById(R.id.initial_account_details_user_display_image_civ_id);
        chooseDisplayImageCIV = findViewById(R.id.initial_account_details_choose_display_image_civ_id);
        emailTIL = findViewById(R.id.initial_account_details_email_tIL_id);
        emailET = findViewById(R.id.initial_account_details_email_tIET_id);
        userNameTIL = findViewById(R.id.initial_account_details_user_name_tIL_id);
        userNameET = findViewById(R.id.initial_account_details_user_name_tIET_id);


        chooseDisplayImageCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }
        });


        chooseStudentTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeType = STUDENT;
                changeMode();
            }
        });

        chooseDoctorTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeType = DOCTOR;
                changeMode();
            }
        });

        sendAccountRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeType == STUDENT){
                    if(registrationNoET.getText().toString().equals("")){
                        registrationNoTIL.setError("Enter your registration no.");
                        return;
                    }else registrationNoTIL.setError(null);

                    if(departmentET.getText().toString().equals("")){
                        departmentTIL.setError("Enter your department");
                        return;
                    }else departmentTIL.setError(null);

                }else {
                    if(doctorDetailsET.getText().toString().equals("")){
                        doctorDetailsTIL.setError("Enter doctor details");
                        return;
                    }else doctorDetailsTIL.setError(null);
                }

                if(userNameET.getText().toString().equals("")){
                    userNameTIL.setError("Enter your user name");
                    return;
                }else emailTIL.setError(null);

                if(emailET.getText().toString().equals("")){
                    emailTIL.setError("Enter your primary email");
                    return;
                }else emailTIL.setError(null);

                if(phoneNoET.getText().toString().equals("")){
                    phoneNoTIL.setError("Enter phone no.");
                    return;
                }else emailTIL.setError(null);

                if(bloodGroupET.getText().toString().equals("")){
                    bloodGroupTIL.setError("Enter your blood group");
                    return;
                }else bloodGroupTIL.setError(null);

                if(imageUri == null){
                    Toast.makeText(InitialAccountDetails.this,"Choose your image",Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User("","","","","","","","","","","","","","","");

                if(modeType == STUDENT){
                    user.setUserType("0");
                    user.setRegistrationNo(registrationNoET.getText().toString());
                    user.setDepartment(departmentET.getText().toString());
                }else {
                    user.setUserType("1");
                    user.setDoctorDetails(doctorDetailsET.getText().toString());
                }

                user.setUserName(userNameET.getText().toString());
                user.setUserUid(userUid);
                user.setEmail(emailET.getText().toString());
                user.setPhoneNo(phoneNoET.getText().toString());
                user.setBloodGroup(bloodGroupET.getText().toString());
                user.setAccountStatus("0");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
                user.setRequestSentDate(dateFormat.format(Timestamp.now().toDate().getTime()));


                final String randomKey = UUID.randomUUID().toString();
                StorageReference imageRef = storage.getReference().child("images/" + randomKey + "." + getFileExtension(imageUri));

                /*
                saving the display image in FirebaseStorage reference.
                 */
                imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                user.setDisplayImageUrl(uri.toString());
                                /*
                                creating a new user document in "users" collection
                                with the given account details
                                 */
                                db.collection("users")
                                        .document(userUid)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(InitialAccountDetails.this, "Request Sent Successfully", Toast.LENGTH_LONG).show();
                                                mAuth.signOut();
                                                onBackPressed();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(InitialAccountDetails.this, "Failed to Send Request", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InitialAccountDetails.this, "Failed to Send Request", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    /**
     * method to get the extension of the display image file
     */
    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }


    public void changeMode(){
        registrationNoTIL.setVisibility(modeType==STUDENT? View.VISIBLE : View.GONE);
        departmentTIL.setVisibility(modeType==STUDENT? View.VISIBLE : View.GONE);
        doctorDetailsTIL.setVisibility(modeType==STUDENT? View.GONE : View.VISIBLE);
        if(modeType == STUDENT){
            chooseStudentTypeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.material_A700));
            chooseStudentTypeButton.setTextColor(ContextCompat.getColor(this,R.color.white));
            chooseDoctorTypeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            chooseDoctorTypeButton.setTextColor(ContextCompat.getColor(this,R.color.black));
        }else {
            chooseDoctorTypeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.material_A700));
            chooseDoctorTypeButton.setTextColor(ContextCompat.getColor(this,R.color.white));
            chooseStudentTypeButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            chooseStudentTypeButton.setTextColor(ContextCompat.getColor(this,R.color.black));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            displayImageCIV.setImageURI(imageUri);
        }
    }
}