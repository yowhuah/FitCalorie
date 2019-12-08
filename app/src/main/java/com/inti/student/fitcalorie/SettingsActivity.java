package com.inti.student.fitcalorie;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class SettingsActivity extends AppCompatActivity {

    private Switch mSwitch, nSwitch;
    SharedPref SP;
    public static SharedPreferences mSharedPreferences;
    public static String sPFile = ".fitcalorie";
    DatabaseReference refer, refers;
    Double calories=0.00, bmi;
    String bmiStatus, caloriesText, username, bmiText, intakeText, caloriesIntake, caloriesStatus;
    Double calsBurned = 0.00, eaten = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        mSharedPreferences = getSharedPreferences(sPFile, MODE_PRIVATE);

        mSwitch = (Switch) findViewById(R.id.night_switch);
        if(SP.loadNightModeState() == true){
            mSwitch.setChecked(true);
        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SP.setNightModeState(true);
                    restartApp();
                }
                else {
                    SP.setNightModeState(false);
                    restartApp();
                }
            }
        });

        refer = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refer.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue().toString();
                String age = dataSnapshot.child("age").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String ht = dataSnapshot.child("hts").getValue().toString();
                String wt = dataSnapshot.child("wts").getValue().toString();
                String jobLevel = dataSnapshot.child("jobLevel").getValue().toString();

                Double x = Double.parseDouble(ht);
                Double y = Double.parseDouble(wt);
                Double jL = Double.parseDouble(jobLevel);
                Integer ages = Integer.parseInt(age);

                if (gender.equals("Male"))
                    calories = (88.362 + (13.397 * y) + (4.799 * x) - (5.677 * ages)) * jL;

                else if (gender.equals("Female"))
                    calories = (447.593 + (9.247 * y) + (3.098 * x) - (4.330 * ages)) * jL;

                DecimalFormat df = new DecimalFormat("#.00");
                caloriesText = df.format(calories);

                bmi = y / x / x * 10000;
                bmiText = df.format(bmi);

                if (intakeText == null)
                    intakeText = "0.00";

                if (bmi < 18.5)
                    bmiStatus = "BMI: " + bmiText + " - Underweight";

                else if (bmi >= 18.5 && bmi <= 24.9)
                    bmiStatus = "BMI: " + bmiText + " - Normal Weight";

                else if (bmi > 24.9 && bmi < 30.0)
                    bmiStatus = "BMI: " + bmiText + " - Overweight";

                else if (bmi >= 30)
                    bmiStatus = "BMI: " + bmiText + " - Obesity";

                else
                    bmiStatus = " Unavailable";


            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, "Fail Connected!", Toast.LENGTH_LONG).show();
            }
        });

        refers = refer.child("FoodIntake");
        refers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    caloriesIntake = dataSnapshot1.child("foodCalorie").getValue().toString();
                    eaten = Double.parseDouble(caloriesIntake);
                    calsBurned = calsBurned + eaten;
                    DecimalFormat df = new DecimalFormat("#.00");
                    intakeText = df.format(calsBurned);

                    caloriesStatus = "Calories Intake: " + intakeText + " / " + caloriesText;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nSwitch = (Switch) findViewById(R.id.notification_switch);
        nSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NotificationCompat.Builder noti = (NotificationCompat.Builder) new NotificationCompat.Builder(SettingsActivity.this)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_transparent))
                            .setSmallIcon(R.drawable.logo_transparent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                            .setContentTitle(username.toUpperCase())
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(bmiStatus + "\n" + caloriesStatus))
                            .setContentText(bmiStatus + "\n" + caloriesStatus);

                    NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nM.notify(1, noti.build());
            }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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

            case android.R.id.home:
                Intent k = new Intent(this, Piechart.class);
                startActivity(k);
                return true;

            case R.id.About:
                Intent x = new Intent(this, AboutActivity.class);
                startActivity(x);
                return true;

            case R.id.Feedback:
                Intent y = new Intent(this, Feedback.class);
                startActivity(y);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restartApp(){
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
        finish();
    }

}
