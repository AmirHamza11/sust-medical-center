package com.example.sustmedicalcenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sustmedicalcenter.controller.MedicineListAdapter;
import com.example.sustmedicalcenter.model.MedicineListModel;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ServiceMedicineActivity extends AppCompatActivity {
    ImageView back_button;
    TextView toolbar_text;
    ArrayList<MedicineListModel> medicineList;
    MedicineListAdapter medicinelistAdapter;
    RecyclerView medicinelistRecyclerview;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton AddMedicineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_medicine);



        back_button = findViewById(R.id.back_button);
        toolbar_text = findViewById(R.id.toolbar_textview);
        medicinelistAdapter = new MedicineListAdapter(getApplicationContext(),medicineList);
        AddMedicineButton = findViewById(R.id.medicine_Button);

        // Back button action of toolbar//

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceMedicineActivity.this,Services.class);
                startActivity(intent);
            }
        });

        //Showing add medicine button only in moderator module//

        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("2")){

            AddMedicineButton.setVisibility(View.VISIBLE);

        }

        initData();
        initmedicinelistRecyclerView();

    }

    private void initData() {

        medicineList=new ArrayList<>();
        medicineList.add(new MedicineListModel("Tuflin 20 mg" , "Tolfenamic Acid"));
        medicineList.add(new MedicineListModel("Rabe 20 mg" , "Omeprazol"));
        medicineList.add(new MedicineListModel("Seclo 20 mg" , "Omeprazol"));
        medicineList.add(new MedicineListModel("Napa 10 mg" , "Paracitamol"));
        medicineList.add(new MedicineListModel("Ace 10 mg" , "Paracitamol"));
        medicineList.add(new MedicineListModel("Tofen 5 mg" , "tofenamic bp"));
        medicineList.add(new MedicineListModel("Zofen 5 mg" , "tofenamic bp"));
        medicineList.add(new MedicineListModel("Zinc 5 mg" , "zinc"));
        medicineList.add(new MedicineListModel("Antacid 3 mg" , "magnesium cloride"));
        medicineList.add(new MedicineListModel("clorofenac 5 mg" , "clorofenamic "));
    }

    private void initmedicinelistRecyclerView() {

        medicinelistRecyclerview = findViewById(R.id.ServiceActivity_recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        medicinelistRecyclerview.setLayoutManager(linearLayoutManager);
        medicinelistAdapter= new MedicineListAdapter(this, medicineList);
        medicinelistRecyclerview.setAdapter(medicinelistAdapter);
        medicinelistAdapter.notifyDataSetChanged();
    }
}