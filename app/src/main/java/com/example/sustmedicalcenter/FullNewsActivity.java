package com.example.sustmedicalcenter;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sustmedicalcenter.model.NewsModel;
import com.squareup.picasso.Picasso;

public class FullNewsActivity extends AppCompatActivity {

    private ImageView newsImage;
    private TextView newsHeadingTV, newsContentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_full);

        NewsModel news = (NewsModel) getIntent().getSerializableExtra("news");

        newsImage = findViewById(R.id.Full_News_Imageview);
        newsHeadingTV = findViewById(R.id.Full_News_Headline);
        newsContentTV = findViewById(R.id.Full_News_Content);

        newsHeadingTV.setText(news.getNewsTitle());
        newsContentTV.setText(news.getNewsContent());

        //Fetching image from Firestore//

        if(!news.getNewsImage().equals("")){
            Picasso.get().load(news.getNewsImage()).into(newsImage);
        }

    }
}