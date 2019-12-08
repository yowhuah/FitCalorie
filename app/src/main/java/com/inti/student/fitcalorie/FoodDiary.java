package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodDiary extends AppCompatActivity {

    SharedPref SP;
    DatabaseReference dbRefer;
    RecyclerView mRecyclerView;
    ArrayList<Food> list;
    FoodDiaryAdapter fAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Food Diary");

        mRecyclerView = (RecyclerView) findViewById(R.id.diaryView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        list = new ArrayList<Food>();

        dbRefer = FirebaseDatabase.getInstance().getReference()
                 .child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FoodIntake");
        dbRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Food f = dataSnapshot1.getValue(Food.class);
                    list.add(f);
                }

                fAdapter = new FoodDiaryAdapter(FoodDiary.this, list);
                mRecyclerView.setAdapter(fAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodDiary.this, "Connection Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFood:
                Intent i = new Intent(this, AddFood.class);
                startActivity(i);
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
