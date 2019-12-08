package com.inti.student.fitcalorie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.DecimalFormat;

public class Piechart extends AppCompatActivity {

    SharedPref SP;
    TextView bmiView, userView;
    Double calories=0.00, bmi, eaten;
    Double calsBurned = 0.00;
    String bmiStatus, caloriesText, intakeText, bmiText;
    DatabaseReference refer, refers;
    String caloriesIntake;
    FloatingActionButton mFloatingActionButton;
    ImageView screenshotView;
    private ConstraintLayout rootContent;
    private DrawerLayout dL;
    private ActionBarDrawerToggle tgl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dL = (DrawerLayout) findViewById(R.id.dL);
        tgl = new ActionBarDrawerToggle(Piechart.this,dL, R.string.Open, R.string.Close);
        tgl.setDrawerIndicatorEnabled(true);

        dL.addDrawerListener(tgl);
        tgl.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Profile:
                        Intent i = new Intent(Piechart.this, ProfileActivity.class);
                        startActivity(i);
                        return true;

                    case R.id.Setting:
                        Intent j = new Intent(Piechart.this, SettingsActivity.class);
                        startActivity(j);
                        return true;

                    case R.id.About:
                        Intent x = new Intent(Piechart.this, AboutActivity.class);
                        startActivity(x);
                        return true;

                    case R.id.Feedback:
                        Intent y = new Intent(Piechart.this, Feedback.class);
                        startActivity(y);
                        return true;

                    case R.id.Logout:
                        Intent z = new Intent(Piechart.this, MainActivity.class);
                        startActivity(z);
                        return true;

                }
                return false;
            }
        });

        rootContent = (ConstraintLayout) findViewById(R.id.ssMain);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        screenshotView = (ImageView) findViewById(R.id.image_view);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b = ScreenshotUtils.getScreenShot(rootContent);
                showScreenShotImage(b);

                File saveFile = ScreenshotUtils.getMainDirectoryName(Piechart.this);
                File file = ScreenshotUtils.store(b, "screenshot" + ".jpg", saveFile);
                shareScreenshot(file);
            }

            private void showScreenShotImage(Bitmap b) {
                screenshotView.setImageBitmap(b);
            }

            private void shareScreenshot(File file) {
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FitCalorie");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
                intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
                startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
                recreate();
            }

        });

        refer = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refer.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
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

                if (bmi < 18.5)
                    bmiStatus = bmiText + " - Underweight";

                else if (bmi >= 18.5 && bmi <= 24.9)
                    bmiStatus = bmiText + " - Normal Weight";

                else if (bmi > 24.9 && bmi < 30.0)
                    bmiStatus = bmiText + " - Overweight";

                else if (bmi >= 30)
                    bmiStatus = bmiText + " - Obesity";

                else
                    bmiStatus = " Unavailable";

                bmiView = (TextView) findViewById(R.id.bmiView);
                bmiView.setText("BMI: " + bmiStatus);

                userView = (TextView) findViewById(R.id.userView);
                userView.setText("Welcome, " + username.toUpperCase());

                updateChart();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Piechart.this, "Fail Connected!", Toast.LENGTH_LONG).show();
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
                }
                updateChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Piechart.this, "Fail Connected!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void addBurned(View v) {
        updateChart();
    }

    public void addConsumed(View v) {
        updateChart();
    }

    private void updateChart(){
        // Update the text in a center of the chart:
        TextView numberOfCals = findViewById(R.id.caloriesView);
        if (intakeText == null)
            intakeText = "0.00";
        numberOfCals.setText(intakeText + " / " + caloriesText + "\n\nCalories");

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d =  calsBurned / calories;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return tgl.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void diaryClicked(View view) {
        Intent i = new Intent(Piechart.this, FoodDiary.class);
        startActivity(i);
    }
}
