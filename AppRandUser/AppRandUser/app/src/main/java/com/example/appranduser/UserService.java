package com.example.appranduser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("api")
    public Call<User> getUser(@Query("gender") String gender, @Query("nat") String nat);
}
