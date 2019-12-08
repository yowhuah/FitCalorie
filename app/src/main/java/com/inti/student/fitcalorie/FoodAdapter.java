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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    Context mContext;
    ArrayList<Food> foods;
    String calories, title,img;
    DatabaseReference refer;

    public FoodAdapter(Context c, ArrayList<Food> f){
        mContext = c;
        foods = f;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(mContext).inflate(R.layout.food_row, viewGroup, false));
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
        Button btnAddFood;

        public FoodViewHolder(View itemView){
            super(itemView);
            foodTitle = (TextView) itemView.findViewById(R.id.foodTitle);
            foodCalorie = (TextView) itemView.findViewById(R.id.foodCalorie);
            foodImage = (ImageView) itemView.findViewById(R.id.foodImage);
            btnAddFood = (Button) itemView.findViewById(R.id.btnAddFood);
        }

        public void onClick(final int position){
            btnAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check auth get auth id, create new attr name foodeat, insert the added value
                    calories = foods.get(position).getFoodCalorie();
                    title = foods.get(position).getFoodTitle();
                    img = foods.get(position).getFoodImage();

                    Food f = new Food(title, calories, img);
                    refer.child(foods.get(position).getFoodTitle()).setValue(f);
                    Toast.makeText(itemView.getContext(),"Added Successfully.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
