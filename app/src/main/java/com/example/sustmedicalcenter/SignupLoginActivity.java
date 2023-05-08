package com.example.sustmedicalcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustmedicalcenter.model.User;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupLoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private static final int LOG_IN = 1;
    private static final int SIGN_UP = 2;

    private int modeType = 2;
    private TextView firstWelcomeText, secondWelcomeText;
    private TextInputLayout userEmailTIL, passwordTIL, confirmPasswordTIL;
    private TextInputEditText userEmailET, passwordET, confirmPasswordET;
    private MaterialButton signupLoginButton, changeModeButton, forgotPasswordButton;
    private CardView cardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstWelcomeText = findViewById(R.id.signup_first_welcome_textView_id);
        secondWelcomeText = findViewById(R.id.signup_second_welcome_textView_id);
        userEmailTIL = findViewById(R.id.signup_email_textInputLayout_id);
        passwordTIL = findViewById(R.id.signup_password_textInputLayout_id);
        confirmPasswordTIL = findViewById(R.id.signup_confirm_password_textInputLayout_id);
        userEmailET = findViewById(R.id.signup_email_editText_id);
        passwordET = findViewById(R.id.signup_password_editText_id);
        confirmPasswordET = findViewById(R.id.signup_confirm_password_editText_id);
        signupLoginButton = findViewById(R.id.signup_login_button_id);
        changeModeButton = findViewById(R.id.change_mode_button_id);
        cardView = findViewById(R.id.signup_cardView_id);
        forgotPasswordButton = findViewById(R.id.signup_forgot_password_button_id);

        ProgressDialog progressDialog = new ProgressDialog(this);


        signupLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = userEmailET.getText().toString();
                String password = passwordET.getText().toString();

                if(modeType == LOG_IN) {

                    progressDialog.setTitle("Signing In...");

                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        userEmailTIL.setError("Invalid Email Address");
                        return;
                    }else userEmailTIL.setError(null);

                    if(password.length() < 6) {
                        passwordTIL.setError("Password length should be at least 6");
                        return;
                    }else passwordTIL.setError(null);

                    progressDialog.show();


                    /*
                      using FirebaseAuthentication instance to sign in user with
                      given email and password.
                     */
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {


                                        /*
                                        checking if current user's email is verified of not.
                                        if the users want's to get the email again, he can request it
                                        again with the help of a dialog.
                                         */
                                        if(mAuth.getCurrentUser().isEmailVerified()){

                                            /*
                                            getting the user details from FirebaseFirestore "users" collection
                                            of the recently signed in user.
                                             */
                                            db.collection("users")
                                                    .document(mAuth.getCurrentUser().getUid())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()){

                                                                /*
                                                                if the task result exists , that means the user has already created an account
                                                                requested giving his initial data.
                                                                if not then we go to initialAccountDetailsActivity.
                                                                 */
                                                                if(task.getResult().exists()){

                                                                    User user = task.getResult().toObject(User.class);

                                                                    progressDialog.dismiss();

                                                                    /*
                                                                    if the account status is "0" , moderator(s) didn't yet accepted this
                                                                    user's account request.

                                                                    if it's "1" , then the user's account request is accepted and his/her account details
                                                                    are saved to CurrentUserSingleton's instance before sending them to the main/dashboard activity.

                                                                    if it's "2" , then the user's request was denied by a moderator.
                                                                     */
                                                                    if(user.getAccountStatus().equals("0")){
                                                                        Toast.makeText(SignupLoginActivity.this,"You're Account Request isn't Accepted Yet. Kindly Wait.",Toast.LENGTH_LONG).show();
                                                                        mAuth.signOut();

                                                                    }else if(user.getAccountStatus().equals("1")){
                                                                        Toast.makeText(SignupLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                                                        CurrentUserSingleton.getInstance().setCurrentUser(user);
                                                                        Intent i = new Intent(SignupLoginActivity.this,MainActivity.class);
                                                                        startActivity(i);
                                                                        finish();

                                                                    }else if(user.getAccountStatus().equals("2")){
                                                                        Toast.makeText(SignupLoginActivity.this, "Sorry! You're Account is Disabled", Toast.LENGTH_LONG).show();
                                                                        mAuth.signOut();

                                                                    }

                                                                }else{
                                                                    progressDialog.dismiss();
                                                                    Intent i = new Intent(SignupLoginActivity.this,InitialAccountDetails.class);
                                                                    i.putExtra("userUid",mAuth.getCurrentUser().getUid());
                                                                    mAuth.signOut();
                                                                    startActivity(i);

                                                                }
                                                            }
                                                        }
                                                    });



                                        }else{
                                            progressDialog.dismiss();


                                            new MaterialAlertDialogBuilder(SignupLoginActivity.this)
                                                    .setTitle("Email isn't verified!")
                                                    .setCancelable(false)
                                                    .setMessage("Verify your email to log in.\nDo you want to get the verification email again?")
                                                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            sendVerificationEmail();
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            mAuth.signOut();
                                                            dialogInterface.dismiss();
                                                        }
                                                    }).create().show();



                                        }


                                    }else {
                                        progressDialog.dismiss();

                                        Toast.makeText(SignupLoginActivity.this, "Login Failed "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }else {

                    progressDialog.setTitle("Signing Up...");

                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        userEmailTIL.setError("Invalid Email Address");
                        return;
                    }else userEmailTIL.setError(null);

                    if(password.length() < 6) {
                        passwordTIL.setError("Password length should be at least 6");
                        return;
                    }else passwordTIL.setError(null);

                    if(!password.equals(confirmPasswordET.getText().toString())) {
                        confirmPasswordTIL.setError("Password doesn't match");
                        return;
                    }else confirmPasswordTIL.setError(null);

                    progressDialog.show();

                    /*
                    using FirebaseAuthentication instance to sign in user with
                      given email and password.
                     */
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    /*
                                    if this task is successful then we're sending email verification
                                    mail to the given email address.
                                     */
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        sendVerificationEmail();
                                        changeMode();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupLoginActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });



        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Patterns.EMAIL_ADDRESS.matcher(userEmailET.getText().toString()).matches()) {
                    userEmailTIL.setError("Enter your account email");
                    return;
                }else userEmailTIL.setError(null);

                /*
                send mail related to changing password to the given email address.
                 */
                mAuth.sendPasswordResetEmail(userEmailET.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(view.getContext(),"Email Sent",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(view.getContext(),"Failed to send email",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        changeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMode();
            }
        });

        changeMode();
    }


    /**
     * sends email verification mail to the inputted email address
     */
    public void sendVerificationEmail(){

        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupLoginActivity.this,"Verification email sent.\nVerify to log in.",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SignupLoginActivity.this,"Failed to send verification email.\nLogin with You're Credentials to get the email again",Toast.LENGTH_LONG).show();
                        }
                        mAuth.signOut();

                    }
                });



    }

    /**
     * change mode of the layout either to Log In to Sign Up or vise versa.
     */
    private void changeMode(){

        if(modeType == LOG_IN) {

            firstWelcomeText.setText("Hello There,");
            secondWelcomeText.setText("Let's start a new journey");
            confirmPasswordTIL.setVisibility(View.VISIBLE);
            signupLoginButton.setText("Sign Up");
            changeModeButton.setText("Already have an account?");
            userEmailET.setText("");
            passwordET.setText("");
            confirmPasswordET.setText("");
            forgotPasswordButton.setVisibility(View.GONE);

            modeType = SIGN_UP;
        }else {

            firstWelcomeText.setText("Welcome Back,");
            secondWelcomeText.setText("Log In to continue");
            confirmPasswordTIL.setVisibility(View.GONE);
            forgotPasswordButton.setVisibility(View.VISIBLE);
            signupLoginButton.setText("Sign In");
            changeModeButton.setText("Create a new account?");
            userEmailET.setText("");
            passwordET.setText("");

            modeType = LOG_IN;
        }

    }


}