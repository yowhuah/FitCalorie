package com.inti.student.fitcalorie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodDiaryAdapter extends RecyclerView.Adapter<FoodDiaryAdapter.FoodViewHolder> {

    Context mContext;
    ArrayList<Food> foods;
    String calories, title,img;
    int maxId;
    DatabaseReference refer;

    public FoodDiaryAdapter(Context c, ArrayList<Food> f){
        mContext = c;
        foods = f;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(mContext).inflate(R.layout.food_diary_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodTitle.setText(foods.get(position).getFoodTitle());
        holder.foodCalorie.setText(foods.get(position).getFoodCalorie());
        Picasso.get().load(foods.get(position).getFoodImage()).resize(900, 900).into(holder.foodImage);

        refer = FirebaseDatabase.getInstance().getReference().child("Member")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("FoodIntake");

        holder.onClick(position);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder
    {
        TextView foodTitle, foodCalorie;
        ImageView foodImage;
        Button btnDeleteFood;

        public FoodViewHolder(View itemView){
            super(itemView);
            foodTitle = (TextView) itemView.findViewById(R.id.foodTitle);
            foodCalorie = (TextView) itemView.findViewById(R.id.foodCalorie);
            foodImage = (ImageView) itemView.findViewById(R.id.foodImage);
            btnDeleteFood = (Button) itemView.findViewById(R.id.btnDeleteFood);
        }

        public void onClick(final int position){
            btnDeleteFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //restart activity

                    String currentTitle = foods.get(position).getFoodTitle();
                    String currentCalorie = foods.get(position).getFoodCalorie();
                    String currentImage = foods.get(position).getFoodImage();

                    Query query = refer.orderByChild("foodTitle").equalTo(currentTitle);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                ds.getRef().removeValue();
                                ((FoodDiary)mContext).recreate();
                                Toast.makeText(itemView.getContext(),"Delete Successfully.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });
        }
    }
}
