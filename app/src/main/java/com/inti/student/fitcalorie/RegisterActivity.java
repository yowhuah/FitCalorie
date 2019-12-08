package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    SharedPref SP;
    RadioGroup mRadioGroup;
    Member member;
    Object value;
    EditText username_txt, email_txt, age_txt, ht_txt, wt_txt, pass, confirm;
    Double jobLevel;
    String username, password, email, gender, cP, ageText, htText, wtText;
    Float hts, wts;
    Integer age;
    Button registerButton;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Registration");

        final Spinner spinner = findViewById(R.id.spinner_job);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.job_level, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = parent.getItemAtPosition(position);

                switch(position){

                    case 0:
                        break;

                    case 1:
                        jobLevel = 1.30;
                        break;

                    case 2:
                        jobLevel = 1.55;
                        break;

                    case 3:
                        jobLevel = 1.65;
                        break;

                    case 4:
                        jobLevel = 1.80;
                        break;

                    case 5:
                        jobLevel = 2.00;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterActivity.this, "Do not leave empty!", Toast.LENGTH_LONG).show();
            }
        });

        mRadioGroup = findViewById(R.id.radioGroup_gender);
        email_txt = (EditText) findViewById(R.id.emailInput);
        pass = (EditText) findViewById(R.id.passwordInput);
        confirm = (EditText) findViewById(R.id.confirmPasswordInput);
        registerButton = (Button) findViewById(R.id.registerButton);
        username_txt = (EditText) findViewById(R.id.username);
        age_txt = (EditText) findViewById(R.id.age);
        ht_txt = (EditText) findViewById(R.id.height);
        wt_txt = (EditText) findViewById(R.id.weight);

        refer = FirebaseDatabase.getInstance().getReference("Member");
        mFirebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                email = email_txt.getText().toString().trim();
                username = username_txt.getText().toString().trim();
                password = pass.getText().toString().trim();
                cP = confirm.getText().toString().trim();
                age = Integer.parseInt(age_txt.getText().toString().trim());
                hts = Float.parseFloat(ht_txt.getText().toString().trim());
                wts = Float.parseFloat(wt_txt.getText().toString().trim());

                switch(mRadioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_male:
                        gender = "Male";
                        break;

                    case R.id.radio_female:
                        gender = "Female";
                        break;

                    default:
                        break;
                }

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "Please enter username.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Please enter email.", Toast.LENGTH_LONG).show();
                    return;
                }

                mFirebaseAuth.fetchProvidersForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                boolean check = !task.getResult().getProviders().isEmpty();

                              if(check)
                                {
                                    Toast.makeText(getApplicationContext(),"This email has been taken. Please Try again.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });


                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Please enter password.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(cP)){
                    Toast.makeText(RegisterActivity.this, "Please enter confirm password.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(age.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please enter age.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(gender == null){
                    Toast.makeText(RegisterActivity.this, "Please enter gender.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(hts.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please enter height.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(wts.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please enter weight.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(jobLevel == null){
                    Toast.makeText(RegisterActivity.this, "Please enter job level.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password is too short.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!password.equals(cP)){
                    Toast.makeText(RegisterActivity.this, "Please make sure the password match with the confirm password.", Toast.LENGTH_LONG).show();
                    return;
                }

                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        member = new Member(
                                                username, email, gender, age, jobLevel, hts, wts
                                        );
                                        FirebaseDatabase.getInstance().getReference("Member")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed, please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
