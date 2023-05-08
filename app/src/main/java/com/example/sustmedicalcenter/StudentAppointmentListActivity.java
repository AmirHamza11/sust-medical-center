package com.example.sustmedicalcenter;

import android.app.ProgressDialog;
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

import com.example.sustmedicalcenter.controller.PrescriptionListAdapter;
import com.example.sustmedicalcenter.controller.StudentAppointmentAdapter;
import com.example.sustmedicalcenter.dialog.AddMedicineDialog;
import com.example.sustmedicalcenter.dialog.PrescriptionListDialog;
import com.example.sustmedicalcenter.model.Appointment;
import com.example.sustmedicalcenter.model.PrescriptionItem;
import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class StudentAppointmentListActivity extends AppCompatActivity implements StudentAppointmentAdapter.itemOnClickListener, AddMedicineDialog.onItemClickListener, PrescriptionListAdapter.OnItemClicked {

    private RecyclerView dueRecyclerView, pastRecyclerView;
    private ArrayList<Appointment> dueList,pastList;
    private StudentAppointmentAdapter dueAdapter, pastAdapter;
    private FloatingActionButton newAppointmentButton;
    private FirebaseFirestore db;
    private String currentUserUid, fetchingFieldName;
    private ListenerRegistration dueListener, pastListener;
    private PrescriptionListDialog prescriptionListDialog;
    private TextView dueAppointmentsTextView, pastAppointmentsTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_appointment_list);

        newAppointmentButton = findViewById(R.id.student_appointment_new_appointment_button_id);
        db = FirebaseFirestore.getInstance();

        currentUserUid = CurrentUserSingleton.getInstance().getCurrentUser().getUserUid();
        fetchingFieldName = CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0")? "applicantUid" : "doctorUid";

        dueList = new ArrayList<>();
        pastList = new ArrayList<>();

        dueRecyclerView =findViewById(R.id.student_appointment_list_today_appointments_recyclerView_id);
        dueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dueAdapter = new StudentAppointmentAdapter(dueList,this, this, 0);
        dueRecyclerView.setAdapter(dueAdapter);


        pastRecyclerView =findViewById(R.id.student_appointment_list_previous_appointments_recyclerView_id);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastAdapter = new StudentAppointmentAdapter(pastList,this, this, 1);
        pastRecyclerView.setAdapter(pastAdapter);

        dueAppointmentsTextView = findViewById(R.id.text);
        pastAppointmentsTextView = findViewById(R.id.text1);



        newAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentAppointmentListActivity.this, NewAppointmentActivity.class);
                startActivity(intent);
            }
        });


        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("1")){
            newAppointmentButton.setVisibility(View.GONE);
        }


        /*
        adding snapshot listener to fetch user's pending appointments in real-time.
         */
        dueListener = db.collection("appointments")
                .whereEqualTo(fetchingFieldName,currentUserUid)
                .whereEqualTo("appointmentStatus", "0")
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                dueAdapter.addDueItem(dc.getDocument().toObject(Appointment.class));

                            }else if(dc.getType() == DocumentChange.Type.REMOVED){
                                dueAdapter.removeItem(dc.getDocument().toObject(Appointment.class));

                            }
                        }

                        if(dueAdapter.getItemCount() != 0){
                            dueAppointmentsTextView.setVisibility(View.VISIBLE);
                        }


                    }
                });


        /*
        adding snapshot listener to fetch user's already completed appointments in real-time.
         */
        pastListener = db.collection("appointments")
                .whereEqualTo(fetchingFieldName,currentUserUid)
                .whereEqualTo("appointmentStatus", "1")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                pastAdapter.addPastItem(dc.getDocument().toObject(Appointment.class));
                            }

                        }

                        if(pastAdapter.getItemCount() != 0){
                            pastAppointmentsTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dueListener.remove();
        pastListener.remove();
    }

    @Override
    public void onCheckupCompletedClicked(int position, int listType) {

        String appointmentUid = (listType == 0)? dueList.get(position).getAppointmentUid() : pastList.get(position).getAppointmentUid();

        /*
        updating the selected appointment as completed in firestore.
         */
        db.collection("appointments")
                .document(appointmentUid)
                .update("appointmentStatus","1")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("salta", e.getMessage());
                    }
                });

    }

    @Override
    public void onPrescriptionButtonClicked(int position, int listType) {
        ArrayList<PrescriptionItem> prescriptionItems = (listType == 0)? dueList.get(position).getPrescriptionList() : pastList.get(position).getPrescriptionList();
        String appointmentUid = (listType == 0)? dueList.get(position).getAppointmentUid() : pastList.get(position).getAppointmentUid();
        prescriptionListDialog = PrescriptionListDialog.newInstance(this, this, prescriptionItems, appointmentUid, this);
        prescriptionListDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onChatButtonClicked(int position, int listType) {
        Intent intent = new Intent(StudentAppointmentListActivity.this,ChatRoomActivity.class);
        Appointment curAppointment = (listType == 0)? dueList.get(position) : pastList.get(position);
        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0")){
            intent.putExtra("UID",curAppointment.getDoctorUid());
            intent.putExtra("DisplayImageUrl",curAppointment.getDoctorDisplayImageUrl());
            intent.putExtra("UserName",curAppointment.getDoctorsName() + " (Doctor)");
        }else{
            intent.putExtra("UID",curAppointment.getApplicantUid());
            intent.putExtra("DisplayImageUrl",curAppointment.getApplicantsDisplayImageUrl());
            intent.putExtra("UserName",curAppointment.getApplicantsName() + " (Student)");
        }
        startActivity(intent);

    }

    @Override
    public void onOpeningUserProfile(int position, int listType) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        String docId;

        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0")){
            docId = (listType == 0)? dueList.get(position).getDoctorUid() : pastList.get(position).getDoctorUid();
        }else {
            docId = (listType == 0)? dueList.get(position).getApplicantUid() : pastList.get(position).getApplicantUid();
        }

        /*
        fetching the user's profile details.
         */
        db.collection("users").document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            Intent intent = new Intent(StudentAppointmentListActivity.this,OtherUserProfileActivity.class);
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
    public void onAddingNewMedicine(PrescriptionItem item, String appointmentUid) {

        /*
        fetching the respected appointment and adding new medicine in it's prescription list field.
         */
        db.collection("appointments")
                .document(appointmentUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Appointment appointment = task.getResult().toObject(Appointment.class);

                            appointment.getPrescriptionList().add(item);

                            db.collection("appointments")
                                    .document(appointmentUid)
                                    .set(appointment)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            prescriptionListDialog.getAdapter().getList().add(item);
                                            prescriptionListDialog.getAdapter().notifyItemInserted(prescriptionListDialog.getAdapter().getItemCount()-1);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                        }else{
                            Log.d("salta", task.getException().getMessage());

                        }

                    }
                });



    }

    @Override
    public void onDeleteItem(int position) {
        String appointmentUid = prescriptionListDialog.getAppointmentUid();
        PrescriptionItem prescriptionItem = prescriptionListDialog.getAdapter().getList().get(position);

        /*
        fetching the respected appointment and deleting selected medicine from it's prescription list field.
         */
        db.collection("appointments")
                .document(appointmentUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Appointment appointment = task.getResult().toObject(Appointment.class);
                            appointment.getPrescriptionList().remove(position);

                            db.collection("appointments")
                                    .document(appointmentUid)
                                    .set(appointment)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            prescriptionListDialog.getAdapter().getList().remove(position);
                                            prescriptionListDialog.getAdapter().notifyItemRemoved(position);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });


                        }else {

                            Log.d("salta", task.getException().getMessage());

                        }
                    }
                });



    }


}
