package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
   EditText email, pass;
   Button loginButton;
   SharedPref SP;
   FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Sign In");

        email = (EditText) findViewById(R.id.emailInput);
        pass = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button) findViewById(R.id.loginButton);

        mFirebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails = email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if(TextUtils.isEmpty(emails)){
                    Toast.makeText(LoginActivity.this, "Please enter email.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please enter password.", Toast.LENGTH_LONG).show();
                    return;
                }

                mFirebaseAuth.signInWithEmailAndPassword(emails, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Successfully.", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), Piechart.class));

                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong email or password, please try again.", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });

    }
}
