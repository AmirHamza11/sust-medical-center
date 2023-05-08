package com.example.sustmedicalcenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Settings extends AppCompatActivity {
    TextView change_pass,email,about,active_status;
    EditText pass;
    ImageView back_button;
    TextView toolbar_text;
    MaterialButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        change_pass = findViewById(R.id.Password_change_tv);
        pass = findViewById(R.id.Password_change_Et);
        back_button = findViewById(R.id.back_button);
        toolbar_text = findViewById(R.id.toolbar_textview);
        save = findViewById(R.id.Savebutton_settings);
        email = findViewById(R.id.Email_us);
        about = findViewById(R.id.About);
        active_status = findViewById(R.id.change_active_status);

        active_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        // Directing to SUST medical center website by clicking the button//


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.sust.edu/offices/other-offices/14");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });


        // pass change action//

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_pass.setVisibility(View.INVISIBLE);
                pass.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });


        //Toolbar back button action in settings//


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_pass.setVisibility(View.VISIBLE);
                pass.setVisibility(View.INVISIBLE);
                save.setVisibility(View.INVISIBLE);
            }
        });


        //Directing to sending email in SUST Medical center by clicking textview//


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"mdc@sust.edu" });
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, "Send mail..."));
            }
        });

    }
}