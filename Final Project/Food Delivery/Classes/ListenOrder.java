package com.rishi.fooddelivery.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.rishi.fooddelivery.Classes.Activity;
import com.rishi.fooddelivery.Common.Common;
import com.rishi.fooddelivery.Model.Request;
import com.rishi.fooddelivery.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference requests;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Request request = dataSnapshot.getValue(Request.class);
        openNotif(dataSnapshot.getKey(), request);
    }

    private void openNotif(String key, Request request) {
        Intent intent = new Intent(getBaseContext(), Activity.class);
        intent.putExtra("userPhone", request.getPhone());
        PendingIntent contentIntent = PendingIntent
                .getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("foodStatus", "foodStatus", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "foodStatus");
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("food")
                .setContentInfo("your order was updated")
                .setContentText("Order #" + key + " was updated to " + Common.convertCodeToStatus(request.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launch);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}