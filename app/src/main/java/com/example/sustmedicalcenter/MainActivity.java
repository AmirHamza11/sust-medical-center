package com.example.sustmedicalcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sustmedicalcenter.model.Day;
import com.example.sustmedicalcenter.model.Message;
import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.ktx.Firebase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNav;
    MaterialToolbar toolbar;
    final int homeFragId = R.id.dashboard_bottom_nav_home_id;
    final int inboxFragId = R.id.dashboard_bottom_nav_inbox_id;
    final int profileFragId = R.id.dashboard_bottom_nav_profile_id;
    HomeFragment mHomeFragment;
    InboxFragment mInboxFragment;
    ProfileFragment mProfileFragment;
    private FirebaseDatabase rdb;

    FirebaseAuth mAuth;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SUSTMedicalCenter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.dashboard_toolbar_id);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        rdb = FirebaseDatabase.getInstance();




        /*
        going back to SignUpLoginActivity if there is no user currently signed in.
         */
        if(mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, SignupLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if(CurrentUserSingleton.getInstance().getCurrentUser()==null){


                /*
                fetching account details of current user and storing them in CurrentUserSingleton
                instance.
                 */
                db.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(task.getResult().exists()){
                                        CurrentUserSingleton.getInstance().setCurrentUser(task.getResult().toObject(User.class));
                                        initFragments();
                                        setUserPresence();
                                    }else{
                                        mAuth.signOut();
                                        Intent intent = new Intent(MainActivity.this,SignupLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Log.d("MainActivityCurrentUser", task.getException().toString());
                                    Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });

            }else {
                initFragments();
                setUserPresence();
            }


        }




        // manually adding days in days collection
        // run once everyday

//        ArrayList<String> mAvailableTime = new ArrayList<>(Arrays.asList("12:00 PM","12:30 PM","1:00 PM","1:30 PM","2:00 PM","4:00 PM","4:30 PM","5:00 PM","5:30 PM","6:00 PM"));
//        ArrayList<Boolean> mIsAvailable = new ArrayList<>(Arrays.asList(true,true,true,true,true,true,true,true,true,true));
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        String formattedDate = dateFormat.format(calendar.getTimeInMillis());
//        Day day = new Day(formattedDate,mAvailableTime,mIsAvailable);
//        addDaysManually(day);
//
//        for(int i=1;i<30;i++) {
//
//            calendar.add(Calendar.DATE, 1);
//            long nextDayInMillies = calendar.getTimeInMillis();
//            formattedDate = dateFormat.format(nextDayInMillies);
//
//            day = new Day(formattedDate,mAvailableTime,mIsAvailable);
//
//            addDaysManually(day);
//
//
//        }







    }


    /**
     * method to set current user's presence.
     */
    public void setUserPresence(){
        final DatabaseReference myConnectionsRef = rdb.getReference("connections/"+CurrentUserSingleton.getInstance().getCurrentUser().getUserUid());
        final DatabaseReference lastOnlineRef = rdb.getReference("lastOnline/"+CurrentUserSingleton.getInstance().getCurrentUser().getUserUid());
        final DatabaseReference connectedRef = rdb.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if(connected){

                    DatabaseReference con = myConnectionsRef.push();
                    con.onDisconnect().removeValue();
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                    con.setValue(Boolean.TRUE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("maocr", "Listener was cancelled at .info/connected");
            }


        });
    }

    private void initFragments() {


        mInboxFragment = new InboxFragment();
        mProfileFragment = new ProfileFragment();
        mHomeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.dashboard_fragment_container_id,mHomeFragment)
                .commit();

        bottomNav = findViewById(R.id.dashboard_bottom_nav_id);
        bottomNav.setSelectedItemId(R.id.dashboard_bottom_nav_home_id);
        bottomNav.setOnNavigationItemSelectedListener(MainActivity.this);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        switch(item.getItemId()){
            case homeFragId:
                changeFragment(mHomeFragment);
                return true;
            case inboxFragId:
                changeFragment(mInboxFragment);
                return true;
            case profileFragId:
                changeFragment(mProfileFragment);
                return true;
        }
        return false;
    }

    private void changeFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.dashboard_fragment_container_id,fragment)
                .commit();

    }



    @Override
    public void onBackPressed() {
        if(bottomNav.getSelectedItemId() != homeFragId){
            bottomNav.setSelectedItemId(homeFragId);
            return;
        }
        super.onBackPressed();
    }



//    void addDaysManually(Day day) {
//
//        db.collection("days")
//                .add(day)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//
//    }
}

