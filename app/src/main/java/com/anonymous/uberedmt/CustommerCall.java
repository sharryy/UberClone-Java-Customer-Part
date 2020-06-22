package com.anonymous.uberedmt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anonymous.uberedmt.Common.Common;
import com.anonymous.uberedmt.Model.FCMResponse;
import com.anonymous.uberedmt.Model.Notification;
import com.anonymous.uberedmt.Model.Sender;
import com.anonymous.uberedmt.Model.Token;
import com.anonymous.uberedmt.Remote.IFCMService;
import com.anonymous.uberedmt.Remote.IGoogleAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustommerCall extends AppCompatActivity {
    TextView txtTime, txtDistance, txtAddress;
    MediaPlayer mediaPlayer;

    IGoogleAPI mService;
    IFCMService mFCMService;

    Button btnCancel, btnAccept;

    String customerID;

    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custommer_call);

        txtTime = findViewById(R.id.txtTime);
        txtDistance = findViewById(R.id.txtDistance);
        txtAddress = findViewById(R.id.txtAddress);

        btnCancel = findViewById(R.id.btnReject);
        btnAccept = findViewById(R.id.btnAccept);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(customerID)) {
                    cancelBooking(customerID);
                }
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(CustommerCall.this, DriverTracking.class);
                 intent.putExtra("lat", lat);
                 intent.putExtra("lng", lng);
                 intent.putExtra("customerId",customerID);

                 startActivity(intent);
                 finish();
            }
        });

        mService = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);

        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null) {
            lat = getIntent().getDoubleExtra("lat", -1.0);
            lng = getIntent().getDoubleExtra("lng", -1.0);
            customerID = getIntent().getStringExtra("customer");

            getDirection(lat, lng);
        }

    }

    private void cancelBooking(String customerID) {
        Token token = new Token(customerID);

        Notification notification = new Notification("Cancel", "Driver has cancelled your request");
        Sender sender = new Sender(token.getToken(), notification);

        mFCMService.sendMessage(sender).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(CustommerCall.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(CustommerCall.this, "Failed...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Toast.makeText(CustommerCall.this, "Declined.", Toast.LENGTH_SHORT).show();
                finish();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getDirection(double lat, double lng) {
        String requestApi;


        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + Common.mLastLocation.getLatitude() + "," + Common.mLastLocation.getLongitude() + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);

            Log.d("Error:", requestApi);
            mService.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONArray routes = jsonObject.getJSONArray("routes");

                        //After getting routes, just get first element of routes
                        JSONObject object = routes.getJSONObject(0);

                        //After first element, we need get array with name legs
                        JSONArray legs = object.getJSONArray("legs");

                        //Getting first element of legs Array
                        JSONObject legsObject = legs.getJSONObject(0);

                        //Now, get Distance
                        JSONObject distance = legsObject.getJSONObject("distance");
                        txtDistance.setText(distance.getString("text"));

                        //Get TIME
                        JSONObject time = legsObject.getJSONObject("duration");
                        txtTime.setText(time.getString("text"));

                        //Get ADDRESS
                        String address = legsObject.getString("end_address");
                        txtAddress.setText(address);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(CustommerCall.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mediaPlayer.start();
    }
}
