package com.example.sustmedicalcenter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sustmedicalcenter.controller.AccountRequestsAdapter;
import com.example.sustmedicalcenter.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AccountRequestsActivity extends AppCompatActivity implements AccountRequestsAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<User> requestedUsers;
    private AccountRequestsAdapter accountRequestsAdapter;

    private FirebaseFirestore db;
    private ListenerRegistration registration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_requests);

        recyclerView = findViewById(R.id.account_request_recyclerView_id);
        requestedUsers = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        accountRequestsAdapter = new AccountRequestsAdapter(this,requestedUsers,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(accountRequestsAdapter);


        /*
        adding snapshot listener to fetch users who's account requests are pending in real time.
         */
        registration = db.collection("users")
                .whereEqualTo("accountStatus","0")
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.d("arase", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                accountRequestsAdapter.addRequestedUser(dc.getDocument().toObject(User.class));
                                Log.d("ji", dc.getDocument().getId()+" added");
                            }else if(dc.getType()==DocumentChange.Type.REMOVED){
                                accountRequestsAdapter.removeRequestedUser(dc.getDocument().toObject(User.class));
                                Log.d("ji", dc.getDocument().getId()+" removed");
                            }
                        }

                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registration.remove();
    }

    @Override
    public void onItemClicked(int position) {

        Intent intent = new Intent(AccountRequestsActivity.this, AccountRequestsFullAccountDetailsActivity.class);
        intent.putExtra("requestedUser",requestedUsers.get(position));
        startActivity(intent);


    }
}