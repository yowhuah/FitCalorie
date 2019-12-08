package com.inti.student.fitcalorie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddFood extends AppCompatActivity {

    SharedPref SP;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static DatabaseReference dbRefer, dbRefer2, dbRefer3;
    static RecyclerView mRecyclerView, mRecyclerView2, mRecyclerView3;
    static ArrayList<Food> list, list2, list3;
    static FoodAdapter fAdapter, fAdapter2, fAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SP = new SharedPref(this);
        if (SP.loadNightModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().setTitle("Add Food");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent k = new Intent(this, FoodDiary.class);
                startActivity(k);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);
                mRecyclerView = (RecyclerView) rootView.findViewById(R.id.foodView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
                list = new ArrayList<Food>();

                dbRefer = FirebaseDatabase.getInstance().getReference().child("Foods");
                dbRefer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Food f = dataSnapshot1.getValue(Food.class);
                            list.add(f);
                        }

                        fAdapter = new FoodAdapter(getActivity(), list);
                        mRecyclerView.setAdapter(fAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Connection Fail", Toast.LENGTH_LONG).show();
                    }
                });
                return rootView;
            }

            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
                mRecyclerView2 = (RecyclerView) rootView.findViewById(R.id.beverageView);
                mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView2.addItemDecoration(new DividerItemDecoration(mRecyclerView2.getContext(), DividerItemDecoration.VERTICAL));
                list2 = new ArrayList<Food>();
                dbRefer2 = FirebaseDatabase.getInstance().getReference().child("Beverages");
                dbRefer2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Food f2 = dataSnapshot1.getValue(Food.class);
                            list2.add(f2);
                        }

                        fAdapter2 = new FoodAdapter(getActivity(), list2);
                        mRecyclerView2.setAdapter(fAdapter2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Connection Fail", Toast.LENGTH_LONG).show();
                    }
                });
                return rootView;
            }

            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);
                mRecyclerView3 = (RecyclerView) rootView.findViewById(R.id.dessertView);
                mRecyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView3.addItemDecoration(new DividerItemDecoration(mRecyclerView3.getContext(), DividerItemDecoration.VERTICAL));
                list3 = new ArrayList<Food>();
                dbRefer3 = FirebaseDatabase.getInstance().getReference().child("Desserts");
                dbRefer3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Food f3 = dataSnapshot1.getValue(Food.class);
                            list3.add(f3);
                        }

                        fAdapter3 = new FoodAdapter(getActivity(), list3);
                        mRecyclerView3.setAdapter(fAdapter3);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Connection Fail", Toast.LENGTH_LONG).show();
                    }
                });
                return rootView;
            }

            else {
                View rootView = inflater.inflate(R.layout.fragment_add_food, container, false);
                return rootView;
            }
        }
    }

        public class SectionsPagerAdapter extends FragmentPagerAdapter {

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).
                return PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                // Show 3 total pages.
                return 3;
            }
        }
}