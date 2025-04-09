package com.vanshika.ibvltask1.api;
import com.vanshika.ibvltask1.model.LoginRequest;
import com.vanshika.ibvltask1.model.LoginResponse;
import com.vanshika.ibvltask1.model.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/transactions")
    Call<List<Transaction>> getTransactions(@Header("Authorization") String token);
}


