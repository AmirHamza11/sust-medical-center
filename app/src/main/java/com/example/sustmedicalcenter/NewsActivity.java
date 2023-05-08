package com.example.sustmedicalcenter;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sustmedicalcenter.controller.NewsAdapter;
import com.example.sustmedicalcenter.model.NewsModel;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    ArrayList<NewsModel> newsList;
    NewsAdapter newsAdapter;
    RecyclerView newsRecyclerview;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton AddNewsButton;
    ImageView back_button;
    TextView news_text;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsList=new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        AddNewsButton = findViewById(R.id.News_Add_Button);
        back_button = findViewById(R.id.back_button);
        news_text = findViewById(R.id.toolbar_textview);
        newsRecyclerview = findViewById(R.id.News_Activity_recyclerview);


        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        newsRecyclerview.setLayoutManager(linearLayoutManager);
        newsAdapter= new NewsAdapter(NewsActivity.this, newsList);
        newsRecyclerview.setAdapter(newsAdapter);
        newsRecyclerview.setHasFixedSize(true);


        // Adding news to Firestore//


        db.collection("news")
                .orderBy("postingDate")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.d("naf",  error.getMessage());
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                newsAdapter.addNews(dc.getDocument().toObject(NewsModel.class));
                                newsRecyclerview.scrollToPosition(0);
                            }
                        }
                    }
                });




        //Back button action of News UI//

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        //Add button Action//

        AddNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, AddNewsActivity.class);
                startActivity(intent);
            }
        });


        // Making add button visible only in moderator module//

        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("1")){

            AddNewsButton.setVisibility(View.GONE);

        }
        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0")){

            AddNewsButton.setVisibility(View.GONE);

        }


    }


}