package com.taxi.friend.drivers.repository;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    public  static Retrofit createRestClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://igyglwsme3.execute-api.us-east-1.amazonaws.com/prod/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit;
    }

}
