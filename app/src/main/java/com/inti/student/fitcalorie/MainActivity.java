package com.inti.student.fitcalorie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    SharedPref SP;
    CircularProgressButton cirLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cirLoginButton = (CircularProgressButton) findViewById(R.id.circularProgress);
        cirLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, String, String> login = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")) {
                            recreate();
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                };

                cirLoginButton.startAnimation();
                login.execute();
            }
        });
    }

    public void registerClicked(View view) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }
}
