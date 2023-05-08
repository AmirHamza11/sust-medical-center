package com.example.sustmedicalcenter.view;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.controller.InboxPersonAdapter;
import com.example.sustmedicalcenter.model.InboxPerson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxPersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private CircleImageView personImageCIV;
    private View personActiveIndicatorView;
    private TextView personUsernameTV;
    private TextView lastMessageTV;
    private TextView lastMessageDateTV;
    private InboxPersonAdapter.ItemOnClickListener itemOnClickListener;
    private FirebaseDatabase ref;


    public InboxPersonViewHolder(@NonNull @NotNull View itemView, InboxPersonAdapter.ItemOnClickListener itemOnClickListener) {
        super(itemView);

        personImageCIV = itemView.findViewById(R.id.inbox_person_profile_image_id);
        personActiveIndicatorView = itemView.findViewById(R.id.inbox_person_active_indicator_id);
        personUsernameTV = itemView.findViewById(R.id.inbox_person_userName_id);
        lastMessageTV = itemView.findViewById(R.id.inbox_person_last_message_id);
        lastMessageDateTV = itemView.findViewById(R.id.inbox_person_last_message_date_id);
        this.itemOnClickListener = itemOnClickListener;

        itemView.setOnClickListener(this);

    }

    public void bind(InboxPerson inboxPerson){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a",Locale.US);
        if(dateFormat.format(inboxPerson.getLastMessageDate()).equals(dateFormat.format(Timestamp.now().toDate().getTime()))){
            lastMessageDateTV.setText(dateFormat1.format(inboxPerson.getLastMessageDate()));
        }else{
            lastMessageDateTV.setText(dateFormat.format(inboxPerson.getLastMessageDate()));
        }

        if(!inboxPerson.getPersonImageUrl().equals("")){
            Picasso.get().load(inboxPerson.getPersonImageUrl()).placeholder(R.drawable.d1).into(personImageCIV);
        }
        personUsernameTV.setText(inboxPerson.getPersonUserName());
        lastMessageTV.setText(inboxPerson.getLastMessage());

        ref = FirebaseDatabase.getInstance();
        DatabaseReference conRef = ref.getReference().child("connections/"+inboxPerson.getPersonUid());

        conRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    personActiveIndicatorView.setVisibility(View.VISIBLE);
                }else{
                    personActiveIndicatorView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        itemOnClickListener.onItemClick(getBindingAdapterPosition());

    }
}
