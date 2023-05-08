package com.example.sustmedicalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BloodActivity extends AppCompatActivity {

    TextView Apositive,Anegative,Bpositive,Bnegative,Opositive,Onegative,ABpositive;
    ImageView back_button;
    TextView tolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        Apositive = findViewById(R.id.blood_apositive);
        Anegative = findViewById(R.id.blood_anegative);
        Bpositive = findViewById(R.id.blood_bpositive);
        Bnegative = findViewById(R.id.blood_bnegative);
        Opositive = findViewById(R.id.blood_opositive);
        Onegative = findViewById(R.id.blood_onegative);
        ABpositive = findViewById(R.id.blood_abpositive);

        back_button = findViewById(R.id.back_button);
        tolbar_text = findViewById(R.id.toolbar_textview);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        // Action on clicking each blood group button//

        Apositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });

        Anegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });

        Bpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });

        Bnegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });

        Opositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });

        Onegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });

        ABpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodActivity.this, BloodListActivity.class);
                startActivity(intent);

            }
        });
    }
}