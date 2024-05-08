package com.example.apiauth;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("users/")
    Call<List<User>> getData(@Header("Authorization") String authToken);

    @POST("users/")
    Call<CreateUserResponse> createUser(@Header("Authorization") String authToken, @Body CreateUserRequest createUserRequest);

}