package com.rishi.fooddelivery.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.rishi.fooddelivery.Interface.ItemClickListener;
import com.rishi.fooddelivery.Model.Order;
import com.rishi.fooddelivery.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartView extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txt_cart_name,txt_cart_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartView(View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_cart_price=itemView.findViewById(R.id.cart_item_Price);
        img_cart_count=itemView.findViewById(R.id.cart_item_count);

    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<com.rishi.fooddelivery.ViewHolder.CartView>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public com.rishi.fooddelivery.ViewHolder.CartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return  new com.rishi.fooddelivery.ViewHolder.CartView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.rishi.fooddelivery.ViewHolder.CartView holder, int position) {
        TextDrawable textDrawable = TextDrawable.builder().
                buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(textDrawable);

        Locale locale = new Locale("en","US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_cart_price.setText(numberFormat.format(price));

        holder.txt_cart_name.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
