package com.example.aparu.birthday_schedule.API;

import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://10.100.101.127:3000/";

    @POST("auth/verify")
    Call<ResponseBody> verify(@Header("token") String token);

    @POST("user/login")
    Call<VerifyResponse> login(@Header("token")String token);

    @GET("birthdays/getBirthdays")
    Call<BirthdayResponse> getBirthdays(@Header("token") String token);

    @GET("birthdays/getBirthdays")
    Call<BirthdayResponse> getBirthdays(@Header("token") String token,
                                        @Query("fromDate") String fDate,
                                        @Query("toDate") String tDate);

    @GET("templates/getTemplates/")
    Call<TemplateResponse> getTemplates(@Header("token") String token);

    @FormUrlEncoded
    @POST("email/send")
    Call<EmailResponse> send(@Header("token") String token,
                             @Field("templateId") int templateId,
                             @Field("empIds") ArrayList<Integer> empIds);
    @FormUrlEncoded
    @POST("email/schedule")
    Call<EmailResponse> schedule(@Header("token") String token,
                                 @Field("templateId") int templateId,
                                 @Field("empIds") ArrayList<Integer> empIds,
                                 @Field("schedule_date") String date,
                                 @Field("templatetext") String text);

    @GET("scheduled/showList")
    Call<BirthdayResponse> getScheduled(@Header("token")String token);

    @FormUrlEncoded
    @PUT("scheduled/update")
    Call<ResponseBody> update(@Header("token") String token,
                              @Field("id") int scheduleId,
                              @Field("templateId") int templateId);
}
