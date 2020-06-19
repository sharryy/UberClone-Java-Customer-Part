package com.anonymous.uberedmt.Remote;

import com.anonymous.uberedmt.Model.FCMResponse;
import com.anonymous.uberedmt.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAbj6usa8:APA91bFE2KZnCFqA6f3dL4EttKDWzATtWZhQ-5EHnq1iHfGfMfZXusxG2Z8C17R1vCsZDrhfbz0LR36LmijwrqNz22Q5SGGCIZWsmmqFXGuVY5Ba_QJsNRXI3Q4aAvIB8Ms3aPAg-72S"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
