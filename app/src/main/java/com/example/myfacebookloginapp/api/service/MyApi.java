package com.example.myfacebookloginapp.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyApi {
    @FormUrlEncoded
    @POST("loginToken.php")
    Call<ResponseBody>insertdata(
            @Field("emailid")String email,
            @Field("token")String token
    );
}
