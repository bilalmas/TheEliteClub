package com.bilalmas.eliteclub;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password1;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, Home.class));
            finish();
        }*/
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.editTextusername);
        password1 = (EditText) findViewById(R.id.editTextpassword);
        progressBar = (ProgressBar) findViewById(R.id.progbar);
        mAuth = FirebaseAuth.getInstance();
    }
    public void login(View view){

        String email = username.getText().toString();
        final String password =  password1.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                password1.setError("Password is less than 6 words!");
                            } else {
                                Toast.makeText(MainActivity.this,"Authentication Failed. Please try again" , Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }



    public void signupactivity(View view){

        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void resetactivity(View view){

        Intent intent = new Intent(this,resetpassword.class);
        startActivity(intent);
    }

}
