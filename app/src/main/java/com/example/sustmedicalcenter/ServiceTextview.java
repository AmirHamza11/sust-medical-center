package com.example.sustmedicalcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceTextview extends AppCompatActivity {

    ImageView back_button;
    TextView toolbar_text;
    TextView headline, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_textview);

        back_button = findViewById(R.id.back_button);
        toolbar_text = findViewById(R.id.toolbar_textview);
        headline = findViewById(R.id.service_Headline);
        content = findViewById(R.id.service_Content);


        //ServiceTextview toolbar back button action//

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceTextview.this,Services.class);
                startActivity(intent);
            }
        });
    }
}