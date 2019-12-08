package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity{

    SharedPref SP;
    DatabaseReference refer;
    Button applyButton;
    Object value;
    String username, email, gender, age, hts, wts, jobLevel;
    Double upJobLevel;
    Float upHts, upWts;
    Integer upAge;
    EditText ht,wt,ages;
    TextView user, em, sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        final Spinner spinner = findViewById(R.id.spinner_job);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this, R.array.job_level, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = parent.getItemAtPosition(position);

                switch(position){

                    case 0:
                        upJobLevel = 0.00;
                        break;

                    case 1:
                        upJobLevel = 1.30;
                        break;

                    case 2:
                        upJobLevel = 1.55;
                        break;

                    case 3:
                        upJobLevel = 1.65;
                        break;

                    case 4:
                        upJobLevel = 1.80;
                        break;

                    case 5:
                        upJobLevel = 2.00;
                        break;

                    default:
                        upJobLevel = 0.00;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ProfileActivity.this, "Do not leave empty!", Toast.LENGTH_LONG).show();
            }
        });

        refer = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refer.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                gender = dataSnapshot.child("gender").getValue().toString();
                age = dataSnapshot.child("age").getValue().toString();
                hts = dataSnapshot.child("hts").getValue().toString();
                wts = dataSnapshot.child("wts").getValue().toString();
                jobLevel = dataSnapshot.child("jobLevel").getValue().toString();

                user = (TextView) findViewById(R.id.username);
                user.setText(username);
                user.setTextSize(17);

                em = (TextView) findViewById(R.id.email);
                em.setText(email);
                em.setTextSize(17);

                sex = (TextView) findViewById(R.id.gender);
                sex.setText(gender);
                sex.setTextSize(17);

                ages = (EditText)findViewById(R.id.age);
                ages.setText(age);

                ht = (EditText)findViewById(R.id.height);
                ht.setText(hts);

                wt = (EditText)findViewById(R.id.weight);
                wt.setText(wts);

                switch(jobLevel){
                    case "0":
                        spinner.setSelection(0);
                        break;

                    case "1.3":
                        spinner.setSelection(1);
                        break;

                    case "1.55":
                        spinner.setSelection(2);
                        break;

                    case "1.65":
                        spinner.setSelection(3);
                        break;

                    case "1.8":
                        spinner.setSelection(4);
                        break;

                    case "2":
                        spinner.setSelection(5);
                        break;
                }


                applyButton = (Button) findViewById(R.id.button_apply);
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upAge = Integer.parseInt(ages.getText().toString().trim());
                        upHts = Float.parseFloat(ht.getText().toString().trim());
                        upWts = Float.parseFloat(wt.getText().toString().trim());

                        dataSnapshot.getRef().child("age").setValue(upAge);
                        dataSnapshot.getRef().child("hts").setValue(upHts);
                        dataSnapshot.getRef().child("wts").setValue(upWts);
                        dataSnapshot.getRef().child("jobLevel").setValue(upJobLevel);

                        Toast.makeText(ProfileActivity.this, "Update Successfully", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                    }
                });

            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Fail Connected!", Toast.LENGTH_LONG).show();
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
