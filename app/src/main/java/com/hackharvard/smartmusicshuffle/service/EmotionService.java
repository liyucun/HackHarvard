package com.hackharvard.smartmusicshuffle.service;

import com.hackharvard.smartmusicshuffle.response.EmotionResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmotionService {

    @POST("")
    Call<EmotionResponse> postPicture(@Body RequestBody photo);
}
