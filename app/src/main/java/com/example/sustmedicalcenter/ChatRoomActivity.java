package com.example.sustmedicalcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sustmedicalcenter.controller.ChatRoomAdapter;
import com.example.sustmedicalcenter.model.InboxPerson;
import com.example.sustmedicalcenter.model.Message;
import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {

    private RecyclerView chatRoomRecyclerView;
    private ArrayList<Message> messages;
    private MaterialToolbar toolbar;
    private ImageButton backImageButton;
    private FirebaseFirestore db;
    private ChatRoomAdapter chatRoomAdapter;
    private String personUserUid;
    private String personDisplayImageUrl;
    private String personUserName;
    private TextView personUserNameTextView;
    private CircleImageView personProfileImageCIV;
    private MaterialButton sendMessageButton;
    private TextInputEditText messageTextEditText;
    private FirebaseDatabase ref;
    private CircleImageView personActiveIndicatorCIV;
    private TextView personLastActiveTextView;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        toolbar = findViewById(R.id.chat_room_toolbar_id);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();

        personUserNameTextView = findViewById(R.id.chat_room_person_user_name_id);
        personProfileImageCIV = findViewById(R.id.chat_room_person_profile_image_id);
        sendMessageButton = findViewById(R.id.chat_room_send_button_id);
        messageTextEditText = findViewById(R.id.chat_room_message_edit_text);
        personActiveIndicatorCIV = findViewById(R.id.chat_room_person_active_indicator_id);
        personLastActiveTextView = findViewById(R.id.chat_room_person_last_active_textView_id);




        backImageButton = findViewById(R.id.chat_room_back_arrow_image_button_id);
        personUserUid = getIntent().getStringExtra("UID");
        personDisplayImageUrl = getIntent().getStringExtra("DisplayImageUrl");
        personUserName = getIntent().getStringExtra("UserName");

        Log.d("chat", personUserUid +" "+ personUserName);


        messages = new ArrayList<>();
        chatRoomRecyclerView = findViewById(R.id.chat_room_recycler_view_id);
        chatRoomRecyclerView.setHasFixedSize(true);
        chatRoomRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        chatRoomAdapter = new ChatRoomAdapter(this,messages);
        chatRoomRecyclerView.setAdapter(chatRoomAdapter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a",Locale.US);




        personUserNameTextView.setText(personUserName);
        if(!personDisplayImageUrl.equals(""))Picasso.get().load(personDisplayImageUrl).placeholder(R.drawable.d1).into(personProfileImageCIV);

        ref = FirebaseDatabase.getInstance();
        DatabaseReference conRef = ref.getReference().child("connections/"+personUserUid);


        /*
        checking if the current user is online. If not online, getting is last online time.
         */
        conRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    personActiveIndicatorCIV.setVisibility(View.VISIBLE);
                    personLastActiveTextView.setText("Active Now");
                }else{
                    personActiveIndicatorCIV.setVisibility(View.GONE);
                    ref.getReference().child("lastOnline/"+personUserUid).get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        Long lastOnlineTime = task.getResult().getValue(Long.class);
                                        if(dateFormat.format(lastOnlineTime).equals(dateFormat.format(Timestamp.now().toDate().getTime()))){
                                            personLastActiveTextView.setText(dateFormat1.format(lastOnlineTime));
                                        }else{
                                            personLastActiveTextView.setText(dateFormat.format(lastOnlineTime));
                                        }

                                    }else{
                                        Log.d("hh", task.getException().getMessage());

                                    }
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        /*
        fetching messages in realtime ordering by the decreasing message sent time.
         */
        registration = db.collection("users")
                .document(CurrentUserSingleton.getInstance().getCurrentUser().getUserUid())
                .collection("inbox")
                .document(personUserUid)
                .collection("message")
                .orderBy("sentTimeInMillies")
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.d("chatRoomListener", "onEvent: ");
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType()== DocumentChange.Type.ADDED){
                                Message msg = dc.getDocument().toObject(Message.class);
                                chatRoomAdapter.addMessage(msg);
                            }

                        }
                    }
                });


        messageTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    sendMessageButton.setEnabled(true);
                }else {
                    sendMessageButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!messageTextEditText.getText().toString().equals("")){

                    Long currentTime = Timestamp.now().toDate().getTime();
                    User currentUser = CurrentUserSingleton.getInstance().getCurrentUser();

                    Message msg = new Message(1,messageTextEditText.getText().toString(),
                            currentTime);

                    /*
                    adding new message to the current user's messages collection
                     */
                    db.collection("users")
                            .document(currentUser.getUserUid())
                            .collection("inbox")
                            .document(personUserUid)
                            .collection("message")
                            .add(msg)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("crnmss", "new message sent successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("crnmsf", e.getMessage());

                                }
                            });

                    msg.setMessageType(0);

                    /*
                    adding new message to receiving user's messages collection
                     */
                    db.collection("users")
                            .document(personUserUid)
                            .collection("inbox")
                            .document(currentUser.getUserUid())
                            .collection("message")
                            .add(msg)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("crnmss2", "new message sent successfully");
                                    chatRoomRecyclerView.smoothScrollToPosition(0);
                                    messageTextEditText.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("crnmsf2", e.getMessage());

                                }
                            });



                    InboxPerson ip = new InboxPerson(personDisplayImageUrl,personUserName,
                            messageTextEditText.getText().toString(), currentTime,
                            personUserUid);

                    /*
                    setting the last message info in current user's inbox collection.
                     */
                    db.collection("users")
                            .document(currentUser.getUserUid())
                            .collection("inbox")
                            .document(personUserUid)
                            .set(ip)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("crnmips", "done");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("crnmipe", e.getMessage());
                                }
                            });

                    String currUserType = CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0") ? " (Student)" : " (Doctor)";

                    ip = new InboxPerson(currentUser.getDisplayImageUrl(), currentUser.getUserName()+currUserType,
                            messageTextEditText.getText().toString(),currentTime,
                            currentUser.getUserUid());

                    /*
                    setting the last message info in receiving user's inbox collection.
                     */
                    db.collection("users")
                            .document(personUserUid)
                            .collection("inbox")
                            .document(currentUser.getUserUid())
                            .set(ip)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("crnmips2", "done");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("crnmipe2", e.getMessage());
                                }
                            });


                }

            }
        });


        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registration.remove();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_room_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.chat_room_info_menu_option_id){
            Toast.makeText(this,"info", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}