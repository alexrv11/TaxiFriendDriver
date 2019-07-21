package com.taxi.friend.drivers.repository;

import com.taxi.friend.drivers.models.Credit;
import com.taxi.friend.drivers.models.LoadCredit;
import com.taxi.friend.drivers.models.Qr;
import com.taxi.friend.drivers.models.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QrRepository {


    @POST("qr")
    Call<ResponseWrapper<Credit>> loadCreditQr(@Body LoadCredit credit);

    @GET("qr/{code}")
    Call<ResponseWrapper<Qr>> getQrDetails(@Path("code") String code);
}
