package com.example.sustmedicalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.controller.BloodListAdapter;
import com.example.sustmedicalcenter.model.BloodModel;

import java.util.ArrayList;

public class BloodListActivity extends AppCompatActivity {

    ArrayList<BloodModel> bloodList;
    BloodListAdapter bloodListAdapter;
    RecyclerView bloodRecyclerview;
    LinearLayoutManager linearLayoutManager;
    ImageView back_button;
    TextView toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_list);

        back_button = findViewById(R.id.back_button);
        toolbar_text = findViewById(R.id.toolbar_textview);
        bloodListAdapter = new BloodListAdapter(getApplicationContext(),bloodList);


        //Toolbar back button action of Blood Group List//

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodListActivity.this, BloodActivity.class);
                startActivity(intent);
            }
        });

        initData();
        initbloodRecyclerview();

    }



    private void initData() {
        bloodList=new ArrayList<>();
        bloodList.add(new BloodModel("Ishrat","0189999078","Last day of donation: 7th dec", R.drawable.ishrat));
        bloodList.add(new BloodModel("Peo","0189799078","Last day of donation: 6th dec", R.drawable.ishrat));
        bloodList.add(new BloodModel("Hamza","0175999078","Last day of donation: 8th dec", R.drawable.hamza));
        bloodList.add(new BloodModel("Badhon","0189769078","Last day of donation: 8th dec", R.drawable.hamza));
        bloodList.add(new BloodModel("Muhit","0189769078","Last day of donation: 8th dec", R.drawable.hamza));
        bloodList.add(new BloodModel("Ifty","0189769078","Last day of donation: 8th dec", R.drawable.hamza));
        bloodList.add(new BloodModel("Meno","0189769078","Last day of donation: 8th dec", R.drawable.ishrat));
        bloodList.add(new BloodModel("Mili","0189769078","Last day of donation: 8th dec", R.drawable.ishrat));
    }


    private void initbloodRecyclerview() {
        bloodRecyclerview = findViewById(R.id.Blood_List_Activity_recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        bloodRecyclerview.setLayoutManager(linearLayoutManager);
        bloodListAdapter= new BloodListAdapter(this, bloodList);
        bloodRecyclerview.setAdapter(bloodListAdapter);
        bloodListAdapter.notifyDataSetChanged();
    }
}