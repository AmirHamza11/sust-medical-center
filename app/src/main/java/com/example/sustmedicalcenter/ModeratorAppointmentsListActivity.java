package com.example.sustmedicalcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sustmedicalcenter.controller.DoctorListAdapter;
import com.example.sustmedicalcenter.model.Doctor;
import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModeratorAppointmentsListActivity extends AppCompatActivity implements DoctorListAdapter.ItemOnClickListener {

    DoctorListAdapter mDoctorListAdapter;
    ArrayList<User>doctors;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_appointments_list);

        db = FirebaseFirestore.getInstance();
        doctors = new ArrayList<>();


        RecyclerView recyclerView = findViewById(R.id.appointments_list_recyclerView_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mDoctorListAdapter = new DoctorListAdapter(this,doctors,this);
        recyclerView.setAdapter(mDoctorListAdapter);

        /*
        fetching doctors' documents to show all the doctors.
         */
        db.collection("users")
                .whereEqualTo("userType","1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            for(DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                User user = snapshot.toObject(User.class);
                                doctors.add(user);
                            }
                            mDoctorListAdapter.setDoctors(doctors);
                            mDoctorListAdapter.notifyDataSetChanged();
                        }else {
                            Log.d("NewAppointmentDoctors", task.getException().toString());
                        }
                    }
                });


    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ModeratorAppointmentsListActivity.this, DoctorAllScheduleActivity.class);
        intent.putExtra("DoctorUid",doctors.get(position).getUserUid());

        startActivity(intent);

    }
}