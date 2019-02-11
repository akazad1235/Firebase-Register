package com.example.akazad.farebasesignup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpEmailEditText, signUpPasswordEditText;
    private TextView signInTextView;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();

        signUpEmailEditText=findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText=findViewById(R.id.SignUpPasswordEditTextId);
        signUpButton=findViewById(R.id.signUpButtonId);
        signInTextView=findViewById(R.id.signInTextviewId);
        progressBar=findViewById(R.id.progressbarId);

        signInTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.signUpButtonId:
               userRegister();
           break;

           case R.id.signInTextviewId:
               Intent intent=new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);
               break;

       }
    }

    private void userRegister() {

        String email=signUpEmailEditText.getText().toString().trim();
        String password=signUpPasswordEditText.getText().toString().trim();

        if (email.isEmpty()){
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()){
             signUpPasswordEditText.setError("Enter a password");
             signUpPasswordEditText.requestFocus();
        }
        if (password.length()<6){
            signUpPasswordEditText.setError("Minimum length of a password should be 6 charecter");
            signUpPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Register is successfully", Toast.LENGTH_SHORT).show();

                } else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User Already Resigter", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error!!!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


}
