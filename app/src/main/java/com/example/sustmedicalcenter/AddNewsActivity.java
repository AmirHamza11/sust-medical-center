package com.example.sustmedicalcenter;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sustmedicalcenter.model.NewsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddNewsActivity extends AppCompatActivity {

    EditText addHeadline, addContent;
    MaterialButton post, upload;
    ImageView uploadImage;
    private Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        addHeadline = findViewById(R.id.Edittext_Headline);
        addContent = findViewById(R.id.EdittextNews_Content);
        post = findViewById(R.id.Post_button_id);
        uploadImage = findViewById(R.id.Add_News_Imageview);
        upload = findViewById(R.id.upload_image_button);
        storage  = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        //Action of Image upload in news creation//

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoseImage();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoseImage();
            }
        });


        //Posting news that have been created//

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri == null) {
                    Toast.makeText(view.getContext(), "choose image",Toast.LENGTH_LONG).show();
                    return;
                }

                if(addHeadline.getText().toString().equals("")){
                    addHeadline.setError("Field must not be empty");
                    return;
                }else addHeadline.setError(null);

                if(addContent.getText().toString().equals("")){
                    addContent.setError("Field must not be empty");
                    return;
                }else addContent.setError(null);

                uploadNewPost();

            }
        });




    }


    //Uploading image in Firebase storage And adding the URL to Firestore database//


    public void ChoseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImage.setImageURI(imageUri);

        }

    }

    private void uploadNewPost() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();


        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/" + randomKey + "." + getFileExtension(imageUri));

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        NewsModel news = new NewsModel(addHeadline.getText().toString(), addContent.getText().toString(),uri.toString(), Timestamp.now().toDate().getTime());

                        db.collection("news")
                                .add(news)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(),"Posted Successfully",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri imageUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }
}