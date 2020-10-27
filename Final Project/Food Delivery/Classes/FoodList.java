package com.rishi.fooddelivery.Classes;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rishi.fooddelivery.Interface.ItemClickListener;
import com.rishi.fooddelivery.Model.Food;
import com.rishi.fooddelivery.R;
import com.daksh.kuro.fooddelivery.ViewHolder.FoodView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodlist;

    FirebaseRecyclerAdapter<Food, FoodView> adapter;
    FirebaseRecyclerAdapter<Food, FoodView> searchAdapter;
    List<String> suggestList = new ArrayList<>();

    @BindView(R.id.search_bar)
    MaterialSearchBar materialSearchBar;

    @BindView(R.id.recycler_food)
    RecyclerView recycler_food;

    RecyclerView.LayoutManager layoutManager;

    String categoryid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Food");
        foodlist.keepSynced(true);

        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);

        if(getIntent()!=null)
        {
            categoryid=getIntent().getStringExtra("CategoryId");
        }
        if(!categoryid.isEmpty() && categoryid !=null)
        {
            loadlistFood(categoryid);
        }

        materialSearchBar.setHint("Search For Food");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When Search Bar is Close
                //Restore original suggest adapter
                if(!enabled)
                    recycler_food.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text){


        FirebaseRecyclerOptions<Food> op = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodlist.orderByChild("name").equalTo(text.toString()),Food.class).build();
        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodView>(op) {
            @NonNull
            @Override
            public FoodView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodView(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull FoodView holder, int position, @NonNull Food model) {
                holder.food_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.foodimage);
                final Food local = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fd = new Intent(com.rishi.fooddelivery.Classes.FoodList.this, com.rishi.fooddelivery.Classes.FoodDetail.class);
                        fd.putExtra("FoodId",searchAdapter.getRef(position).getKey());
                        startActivity(fd);
                    }
                });
            }
        };
        recycler_food.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }

    private void loadSuggest() {
        foodlist.orderByChild("MenuId").equalTo(categoryid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadlistFood(String categoryid) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(foodlist.orderByChild("MenuId").equalTo(categoryid), Food.class)
                        .build();
        adapter= new FirebaseRecyclerAdapter<Food, FoodView>(options)
        {
            @NonNull
            @Override
            public FoodView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);

                return new FoodView(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull FoodView viewHolder, int position, @NonNull Food model) {
                viewHolder.food_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.foodimage);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(com.rishi.fooddelivery.Classes.FoodList.this, com.rishi.fooddelivery.Classes.FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recycler_food.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
