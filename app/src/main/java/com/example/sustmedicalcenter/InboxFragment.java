package com.example.sustmedicalcenter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sustmedicalcenter.controller.InboxPersonAdapter;
import com.example.sustmedicalcenter.model.InboxPerson;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class InboxFragment extends Fragment implements InboxPersonAdapter.ItemOnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<InboxPerson> inboxPersons;
    FirebaseFirestore db;
    InboxPersonAdapter inboxPersonAdapter;
    ListenerRegistration registration;

    //Required
    public InboxFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        inboxPersons = new ArrayList<>();

        recyclerView = view.findViewById(R.id.inbox_recyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inboxPersonAdapter = new InboxPersonAdapter(getContext(),inboxPersons,this);
        recyclerView.setAdapter(inboxPersonAdapter);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        /*
        adding a snapshot listener to all the persons in current user's inbox
        and fetching them by increasing order of their last message date.
         */
        registration = db.collection("users")
                .document(CurrentUserSingleton.getInstance().getCurrentUser().getUserUid())
                .collection("inbox")
                .orderBy("lastMessageDate")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.d("ifovce", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType()== DocumentChange.Type.ADDED){
                                InboxPerson ip = dc.getDocument().toObject(InboxPerson.class);
                                inboxPersonAdapter.addInboxPerson(ip);

                            }else if(dc.getType() == DocumentChange.Type.MODIFIED){
                                InboxPerson ip = dc.getDocument().toObject(InboxPerson.class);
                                inboxPersonAdapter.modifyInboxPerson(ip);
                            }

                        }

                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        registration.remove();
    }



    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.inbox_fragment_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
  
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId()==R.id.inbox_fragment_search_menu_option_id){
            Toast.makeText(getContext(),"Search",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }    

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(),ChatRoomActivity.class);
        intent.putExtra("UID",inboxPersons.get(position).getPersonUid());
        intent.putExtra("DisplayImageUrl",inboxPersons.get(position).getPersonImageUrl());
        intent.putExtra("UserName",inboxPersons.get(position).getPersonUserName());
        startActivity(intent);

    }
}