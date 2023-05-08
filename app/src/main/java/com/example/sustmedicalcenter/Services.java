package com.example.sustmedicalcenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Services extends AppCompatActivity {
    ImageView back_button;
    TextView toolbar_text;
    CardView treatment, medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        back_button = findViewById(R.id.back_button);
        toolbar_text = findViewById(R.id.toolbar_textview);
        treatment = findViewById(R.id.service_cardview);
        medicine = findViewById(R.id.medicine_cardview);


        //Services toolbar back button action//

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Services.this,MainActivity.class);
                startActivity(intent);
            }
        });



        treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Services.this,ServiceTextview.class);
                startActivity(intent);

            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Services.this,ServiceMedicineActivity.class);
                startActivity(intent);

            }
        });
    }
}