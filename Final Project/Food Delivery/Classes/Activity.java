package com.rishi.fooddelivery.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rishi.fooddelivery.Common.Common;
import com.rishi.fooddelivery.Model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rishi.foodelivery.R;

public class Activity extends AppCompatActivity {

    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, com.rishi.fooddelivery.ViewHolder.OrderView> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");


        recyclerView = (RecyclerView)findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (getIntent().getExtras() == null)
            loadTheOrders(Common.currentUser.getPhone());
        else
            loadTheOrders(getIntent().getStringExtra("userPhone"));
    }

    private void loadTheOrders(String phone) {
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(requests.orderByChild("phone").equalTo(phone),Request.class)
                        .build();
        adapter= new FirebaseRecyclerAdapter<Request, com.rishi.fooddelivery.ViewHolder.OrderView>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull com.rishi.fooddelivery.ViewHolder.OrderView holder, int position, @NonNull Request model) {
                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                holder.txtOrderAddress.setText(model.getAddress());
                holder.txtOrderPhone.setText(model.getPhone());

            }

            @NonNull
            @Override
            public com.rishi.fooddelivery.ViewHolder.OrderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new com.rishi.fooddelivery.ViewHolder.OrderView(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }
    private String convertCodeToStatus(String status)
    {
        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On its way";
        else
            return "Delivered";
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
