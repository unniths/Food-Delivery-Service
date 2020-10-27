package com.rishi.fooddelivery.Classes;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.rishi.fooddelivery.R;
import com.rishi.fooddelivery.Service.ListenOrder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rishi.fooddelivery.Common.Common;
import com.rishi.fooddelivery.Interface.ItemClickListener;
import com.rishi.fooddelivery.Model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, com.rishi.fooddelivery.ViewHolder.MenuView> adapter;

    TextView txtFullName;
    @BindView(R.id.recycler_menu)
    RecyclerView recycler_menu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        category.keepSynced(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(com.rishi.fooddelivery.Classes.Home.this, com.rishi.fooddelivery.Classes.CartActivity.class);
                startActivity(cartIntent);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

        Intent service = new Intent(com.rishi.fooddelivery.Classes.Home.this, ListenOrder.class);
        startService(service);

    }

    private void loadMenu() {
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(category, Category.class)
                        .build();
        adapter= new FirebaseRecyclerAdapter<Category, com.rishi.fooddelivery.ViewHolder.MenuView>(options)
        {
            @NonNull
            @Override
            public com.rishi.fooddelivery.ViewHolder.MenuView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);

                return new com.rishi.fooddelivery.ViewHolder.MenuView(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull com.rishi.fooddelivery.ViewHolder.MenuView viewHolder, int position, @NonNull Category model) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get Category Id and Send to new Activity
                        Intent foodlist = new Intent(com.rishi.fooddelivery.Classes.Home.this, com.rishi.fooddelivery.Classes.FoodList.class);
                        foodlist.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodlist);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id= item.getItemId();
        if (id == R.id.nav_menu)
        {
            Intent menu_intent = new Intent(com.rishi.fooddelivery.Classes.Home.this, com.rishi.fooddelivery.Classes.Home.class);
            startActivity(menu_intent);
        }
        else if(id == R.id.nav_cart)
        {
            Intent cartIntent = new Intent(com.rishi.fooddelivery.Classes.Home.this, com.rishi.fooddelivery.Classes.CartActivity.class);
            startActivity(cartIntent);

        }
        else if(id == R.id.nav_orders)
        {
            Intent orderIntent = new Intent(com.rishi.fooddelivery.Classes.Home.this, Activity.class);
            startActivity(orderIntent);
        }
        else if(id == R.id.nav_log_out)
        {
            Intent SignIn= new Intent(com.rishi.fooddelivery.Classes.Home.this, com.rishi.fooddelivery.Classes.MainActivity.class);
            SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(SignIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
