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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
        private EditText signInEmailEditText, signInPasswordEditText;
        private TextView signUpTextView;
        private Button signInButton;
    private FirebaseAuth mAuth;
        public ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         this.setTitle("Sign Up");

        signInEmailEditText=findViewById(R.id.signInEmailEditTextId);
        signInPasswordEditText=findViewById(R.id.SignInPasswordEditTextId);
        signInButton=findViewById(R.id.signInButtonId);
        signUpTextView=findViewById(R.id.signupTextviewId);
        signUpTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        progressBar=findViewById(R.id.progressbarId);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();
        if (user !=null){
            Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.signInButtonId:
                userLogin();
            break;

            case R.id.signupTextviewId:
                Intent intent= new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            break;
        }
    }

    private void userLogin() {
        String email=signInEmailEditText.getText().toString().trim();
        String password=signInPasswordEditText.getText().toString().trim();

        if (email.isEmpty()){
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmailEditText.setError("Enter a valid email address");
            signInEmailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()){
            signInPasswordEditText.setError("Enter a password");
            signInPasswordEditText.requestFocus();
        }
        if (password.length()<6){
            signInPasswordEditText.setError("Minimum length of a password should be 6 charecter");
            signInPasswordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                   // FirebaseUser user=mAuth.getCurrentUser();
                    finish();
                    Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Unseccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
