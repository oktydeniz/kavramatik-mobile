package com.kavramatik.kavramatik.network;

import com.kavramatik.kavramatik.model.ResponseModel;
import com.kavramatik.kavramatik.model.StatusModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @FormUrlEncoded
    @POST("register")
    Call<ResponseModel> register(@Field("user_email") String userMail,
                                 @Field("user_name") String userName,
                                 @Field("user_password") String userPassword);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseModel> login(@Field("user_email") String userMail,
                              @Field("user_password") String userPassword);


    @GET("sendMailForPassword")
    Call<StatusModel> sendMail(@Query("user_email") String userMail);

    @FormUrlEncoded
    @POST("formatPassword")
    Call<ResponseModel> resetPassword(@Field("user_email") String userEmail,
                                      @Field("user_password") String userPassword,
                                      @Field("verification") String verification);


    @FormUrlEncoded
    @POST("setScore")
    Call<ResponseModel> setScoreRequest(@Field("user_email") String userMail, @Field("score") int score);


    @GET("showProfile")
    Call<ResponseModel> showProfile(@Field("user_email") String userMail);
}
