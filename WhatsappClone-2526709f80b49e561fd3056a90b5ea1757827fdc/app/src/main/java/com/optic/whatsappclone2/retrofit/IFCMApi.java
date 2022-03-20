package com.optic.whatsappclone2.retrofit;

import com.optic.whatsappclone2.models.FCMBody;
import com.optic.whatsappclone2.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAWZRfwW4:APA91bGH7ILI6v0eHg8DqDqRNz1QJrMNlNs0mDU0Jnuu1dpPPN11AulxrO1XcnY14Hi5BAZ-YR5zNeV1J-GFuxEx1sumTewOTpEWCqg-8_DvsJZK9VuzsCqhsbqEAe_vNo0KkIQ89Iz8"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
