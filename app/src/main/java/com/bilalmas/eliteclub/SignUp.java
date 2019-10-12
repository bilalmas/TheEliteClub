package com.bilalmas.eliteclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class SignUp extends AppCompatActivity {

    private EditText Name;
    private EditText Emailaddr;
    private EditText passwordfirst;
    private EditText confirmpassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Name = (EditText)findViewById(R.id.editTextname);
        Emailaddr =(EditText)findViewById(R.id.editTextemail);
        passwordfirst = (EditText) findViewById(R.id.editTextpassword);
        confirmpassword = (EditText) findViewById(R.id.editTextconfirmpassword);
        progressBar = (ProgressBar) findViewById(R.id.progbar);
        mAuth = FirebaseAuth.getInstance();

    }
    public void Signup(View view){
      Log.d("Signup","Sigup works!!!!!!!");


            String email = Emailaddr.getText().toString().trim();
            String password = passwordfirst.getText().toString().trim();
            String cpassword = confirmpassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.equals(cpassword)){

                progressBar.setVisibility(View.VISIBLE);
                //create user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                //progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(getApplicationContext(), "Passwords do not match, please enter correctly!", Toast.LENGTH_SHORT).show();
                Log.i("password",password);
                Log.i("password",cpassword);


            }



        }

    public void Login(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

