package com.example.a50beees.data.source;

import com.example.a50beees.data.dto.AccountDto;
import com.example.a50beees.data.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/user")
    Call<List<UserDto>> getAll();
    @GET("/user/{id}")
    Call<UserDto> getById(@Path("id") String id, @Header("Authorization") String authHeader);
    @GET("/user/username/{username}")
    Call<Void> isExist(@Path("username") String login);
    @POST("/user/register")
    Call<Void> register(@Body AccountDto dto);
    @GET("/user/username/{username}")
    Call<UserDto> getByUsername(@Path("username") String username, @Header("Authorization") String authHeader);
    @GET("/user/login")
    Call<Void> login(@Header("Authorization") String authHeader);
}