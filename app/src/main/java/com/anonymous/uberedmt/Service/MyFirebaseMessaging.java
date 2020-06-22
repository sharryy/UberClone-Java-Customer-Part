package com.anonymous.uberedmt.Service;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.anonymous.uberedmt.CustommerCall;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        //Because Firebase push notification will be send containing lat and lng app
        //So it is needed to convert message to Latlng
        LatLng customer_location = new Gson().fromJson(remoteMessage.getNotification().getBody(), LatLng.class);

        Intent intent = new Intent(getBaseContext(), CustommerCall.class);
        intent.putExtra("lat", customer_location.latitude);
        intent.putExtra("lng", customer_location.longitude);
        intent.putExtra("customer", remoteMessage.getNotification().getTitle());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }
}
