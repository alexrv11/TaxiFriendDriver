package com.taxi.friend.drivers.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    public  static Retrofit createRestClient(){

        ObjectMapper mapper = new ObjectMapper().registerModule(new KotlinModule());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .header("User-Agent", "taxi-friend-server")
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });

                OkHttpClient client = httpClient.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://igyglwsme3.execute-api.us-east-1.amazonaws.com/prod/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client)
                .build();

        return retrofit;
    }

}
