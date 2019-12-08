package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    SharedPref SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");
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
