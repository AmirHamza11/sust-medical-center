package com.example.sustmedicalcenter;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private TextView email_textview_profile, dept_textview_profile, reg_textview_profile, PhoneNo_textview_profile, BloodGroup_textview_profile, BloodDonation_textview_profile, ChronicDisease_textview_profile;
    private EditText email_edittext_profile, dept_edittext_profile, reg_edittext_profile, PhoneNo_edittext_profile, BloodGroup_edittext_profile, BloodDonation_edittext_profile, ChronicDisease_edittext_profile;
    private Button EditButton_Profile, SaveButton_Profile;
    private CircleImageView profile_image;
    private   Uri imageUri;
    private FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;
    private TextView userNameTextView;

    FirebaseAuth mAuth;


    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = view.findViewById(R.id.user_profile_display_image_civ_id);
        storage  = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        userNameTextView = view.findViewById(R.id.user_profile_user_name_tv_id);



        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChoseImage();

            }
        });


        email_edittext_profile = view.findViewById(R.id.user_profile_email_et_id);
        dept_edittext_profile = view.findViewById(R.id.user_profile_department_et_id);
        reg_edittext_profile = view.findViewById(R.id.user_profile_registration_no_et_id);
        PhoneNo_edittext_profile = view.findViewById(R.id.user_profile_phone_no_et_id);
        BloodGroup_edittext_profile = view.findViewById(R.id.user_profile_blood_group_et_id);
        BloodDonation_edittext_profile = view.findViewById(R.id.user_profile_last_day_of_blood_donation_et_id);
        ChronicDisease_edittext_profile = view.findViewById(R.id.user_profile_chronic_disease_et_id);

        email_textview_profile = view.findViewById(R.id.user_profile_email_tv_id);
        dept_textview_profile = view.findViewById(R.id.user_profile_department_tv_id);
        reg_textview_profile = view.findViewById(R.id.user_profile_registration_no_tv_id);
        PhoneNo_textview_profile = view.findViewById(R.id.user_profile_phone_no_tv_id);
        BloodGroup_textview_profile = view.findViewById(R.id.user_profile_blood_group_tv_id);
        BloodDonation_textview_profile = view.findViewById(R.id.user_profile_last_day_of_blood_donation_tv_id);
        ChronicDisease_textview_profile = view.findViewById(R.id.user_profile_chronic_disease_tv_id);


        EditButton_Profile = view.findViewById(R.id.EditButton_Profile);
        SaveButton_Profile = view.findViewById(R.id.SaveButton_Profile);

        EditButton_Profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                email_textview_profile.setVisibility(View.INVISIBLE);
                dept_textview_profile.setVisibility(View.INVISIBLE);
                reg_textview_profile.setVisibility(View.INVISIBLE);
                PhoneNo_textview_profile.setVisibility(View.INVISIBLE);
                BloodGroup_textview_profile.setVisibility(View.INVISIBLE);
                BloodDonation_textview_profile.setVisibility(View.INVISIBLE);
                ChronicDisease_textview_profile.setVisibility(View.INVISIBLE);
                EditButton_Profile.setVisibility(View.GONE);


                email_edittext_profile.setVisibility(View.VISIBLE);
                dept_edittext_profile.setVisibility(View.VISIBLE);
                reg_edittext_profile.setVisibility(View.VISIBLE);
                PhoneNo_edittext_profile.setVisibility(View.VISIBLE);
                BloodGroup_edittext_profile.setVisibility(View.VISIBLE);
                BloodDonation_edittext_profile.setVisibility(View.VISIBLE);
                ChronicDisease_edittext_profile.setVisibility(View.VISIBLE);
                SaveButton_Profile.setVisibility(View.VISIBLE);


                email_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getEmail());
                dept_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getDepartment());
                reg_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getRegistrationNo());
                PhoneNo_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getPhoneNo());
                BloodGroup_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getBloodGroup());
                BloodDonation_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getLastDayOfBloodDonation());
                ChronicDisease_edittext_profile.setText(CurrentUserSingleton.getInstance().getCurrentUser().getChronicDisease());

            }


        });



        SaveButton_Profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email_string = email_edittext_profile.getText().toString();
                String dept_string = dept_edittext_profile.getText().toString();
                String reg_string = reg_edittext_profile.getText().toString();
                String PhoneNo_string = PhoneNo_edittext_profile.getText().toString();
                String BloodGroup_string = BloodGroup_edittext_profile.getText().toString();
                String BloodDonation_string = BloodDonation_edittext_profile.getText().toString();
                String ChronicDisease_string = ChronicDisease_edittext_profile.getText().toString();


                email_textview_profile.setText("Email: " + email_string );
                dept_textview_profile.setText("Dept: " + dept_string );
                reg_textview_profile.setText("Reg No: " + reg_string );
                PhoneNo_textview_profile.setText("Phone No: " + PhoneNo_string );
                BloodGroup_textview_profile.setText("Blood Group: " + BloodGroup_string );
                BloodDonation_textview_profile.setText("Last day of blood donation: " + BloodDonation_string );
                ChronicDisease_textview_profile.setText("Chronic Diseases: " + ChronicDisease_string );




                email_edittext_profile.setVisibility(View.INVISIBLE);
                dept_edittext_profile.setVisibility(View.INVISIBLE);
                reg_edittext_profile.setVisibility(View.INVISIBLE);
                PhoneNo_edittext_profile.setVisibility(View.INVISIBLE);
                BloodGroup_edittext_profile.setVisibility(View.INVISIBLE);
                BloodDonation_edittext_profile.setVisibility(View.INVISIBLE);
                ChronicDisease_edittext_profile.setVisibility(View.INVISIBLE);
                SaveButton_Profile.setVisibility(View.GONE);

                email_textview_profile.setVisibility(View.VISIBLE);
                dept_textview_profile.setVisibility(View.VISIBLE);
                reg_textview_profile.setVisibility(View.VISIBLE);
                PhoneNo_textview_profile.setVisibility(View.VISIBLE);
                BloodGroup_textview_profile.setVisibility(View.VISIBLE);
                BloodDonation_textview_profile.setVisibility(View.VISIBLE);
                ChronicDisease_textview_profile.setVisibility(View.VISIBLE);
                EditButton_Profile.setVisibility(View.VISIBLE);

                Save_ProfileData_Firebase();


            }


        });

        return view;


    }


    @Override
    public void onResume() {
        super.onResume();



        User user = CurrentUserSingleton.getInstance().getCurrentUser();

        userNameTextView.setText(user.getUserName());

        email_textview_profile.setText("Email: " + user.getEmail() );
        dept_textview_profile.setText("Dept: " + user.getDepartment() );
        reg_textview_profile.setText("Reg No: " + user.getRegistrationNo() );
        PhoneNo_textview_profile.setText("Phone No: " + user.getPhoneNo() );
        BloodGroup_textview_profile.setText("Blood Group: " + user.getBloodGroup() );
        BloodDonation_textview_profile.setText("Last day of blood donation: " + user.getLastDayOfBloodDonation() );
        ChronicDisease_textview_profile.setText("Chronic Diseases: " + user.getChronicDisease() );

        if(!CurrentUserSingleton.getInstance().getCurrentUser().getDisplayImageUrl().equals("")){
            Picasso.get()
                    .load(CurrentUserSingleton.getInstance().getCurrentUser().getDisplayImageUrl())
                    .placeholder(R.drawable.d1)
                    .into(profile_image);
        }


    }

    public void ChoseImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            uploadImage();

        }

    }


    /**
     * saving display image to firebase storage and then saving the
     * downloadable url to users document in "users" collection.
     */
    private void uploadImage() {

        mAuth = FirebaseAuth.getInstance();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();



        final String randomkey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/" + randomkey + "." + getFileExtension(imageUri));

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        CurrentUserSingleton.getInstance().getCurrentUser().setDisplayImageUrl(uri.toString());
                        db.collection("users")
                                .document(CurrentUserSingleton.getInstance().getCurrentUser().getUserUid())
                                .update("displayImageUrl",uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        progressDialog.dismiss();
                                        Picasso.get()
                                                .load(CurrentUserSingleton.getInstance().getCurrentUser().getDisplayImageUrl())
                                                .placeholder(R.drawable.d1)
                                                .into(profile_image);
                                        Toast.makeText(getActivity(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }


    private String getFileExtension(Uri imageUri) {

        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }


    /**
     * saving edited profile details to database.
     */
    public void Save_ProfileData_Firebase() {

        String Profile_Email_string_firebase = email_edittext_profile.getText().toString().trim();
        String Profile_Dept_string_firebase = dept_edittext_profile.getText().toString().trim();
        String Profile_Reg_string_firebase = reg_edittext_profile.getText().toString().trim();
        String Profile_PhoneNo_string_firebase = PhoneNo_edittext_profile.getText().toString().trim();
        String Profile_BloodGroup_string_firebase = BloodGroup_edittext_profile.getText().toString().trim();
        String Profile_Lastdayofblooddonation_string_firebase = BloodDonation_edittext_profile.getText().toString().trim();
        String Profile_ChronicDisease_string_firebase = ChronicDisease_edittext_profile.getText().toString().trim();




        User user = CurrentUserSingleton.getInstance().getCurrentUser();

        user.setEmail(Profile_Email_string_firebase);
        user.setDepartment(Profile_Dept_string_firebase);
        user.setRegistrationNo(Profile_Reg_string_firebase);
        user.setPhoneNo(Profile_PhoneNo_string_firebase);
        user.setBloodGroup(Profile_BloodGroup_string_firebase);
        user.setLastDayOfBloodDonation(Profile_Lastdayofblooddonation_string_firebase);
        user.setChronicDisease(Profile_ChronicDisease_string_firebase);



        db.collection("users")
                .document(user.getUserUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.profile_fragment_search_menu_option_id:
                Toast.makeText(getContext(),"Search",Toast.LENGTH_SHORT).show();

                return true;

            case R.id.profile_fragment_logout_menu_option_id:

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Account Logout")
                        .setMessage("Are you sure you want to logout from this account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                Intent intent = new Intent(getActivity(), SignupLoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();



                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}