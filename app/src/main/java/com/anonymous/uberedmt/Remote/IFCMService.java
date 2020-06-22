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
            "Authorization:key=AAAABDnmxIU:APA91bHgLyLl2lwwneeSzkrm5T5utRLGAlIgEHEzVld7bPlSv4hjMv3Q4yn7ET8GPHi8rhgNoGyrfA_CHknbKGMt3eUTMHP1OG5aJqIrfZfcAyjWCRyUCsLXWSubw_shtlL4qQ1DfJ2r"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
