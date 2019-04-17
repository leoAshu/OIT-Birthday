package com.example.aparu.birthday_schedule.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private Retrofit retrofit;
    private static RetrofitClient mInstance;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){

        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi(){

        return retrofit.create(Api.class);
    }

}
