package com.daksh.kuro.fooddelivery.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.rishi.fooddelivery.Interface.ItemClickListener;
import com.rishi.fooddelivery.R;

public class FoodView extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView food_name;
    public ImageView foodimage;

    private ItemClickListener itemClickListener;

    public FoodView(View itemView) {
        super(itemView);
        food_name = (TextView)itemView.findViewById(R.id.food_name);
        foodimage = (ImageView)itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
