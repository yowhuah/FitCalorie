package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Feedback extends AppCompatActivity {

    SharedPref SP;
    private EditText mTitle;
    private EditText mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        mTitle = findViewById(R.id.title);
        mMessage = findViewById(R.id.message);

        Button send = findViewById(R.id.send_email);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }

    private void sendMail(){
        String recipient[] = {"yowhuah99@gmail.com"};

        String subject = mTitle.getText().toString();
        String message = mMessage.getText().toString();

        Intent i = new Intent (Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, recipient);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(i, "Choose an email client"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Profile:
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                return true;

            case R.id.Setting:
                Intent j = new Intent(this, SettingsActivity.class);
                startActivity(j);
                return true;

            case R.id.About:
                Intent x = new Intent(this, AboutActivity.class);
                startActivity(x);
                return true;

            case R.id.Feedback:
                Intent y = new Intent(this, Feedback.class);
                startActivity(y);
                return true;

            case android.R.id.home:
                Intent k = new Intent(this, Piechart.class);
                startActivity(k);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
