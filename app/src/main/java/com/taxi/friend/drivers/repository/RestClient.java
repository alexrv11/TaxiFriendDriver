package com.taxi.friend.drivers.repository;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    public  static Retrofit createRestClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.31:8080/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit;
    }

}
