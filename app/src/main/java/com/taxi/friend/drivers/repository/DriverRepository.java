package com.taxi.friend.drivers.repository;

import com.taxi.friend.drivers.models.DriverLocation;
import com.taxi.friend.drivers.models.Location;
import com.taxi.friend.drivers.models.ResponseWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverRepository {

    @GET("drivers")
    Call<ResponseWrapper<DriverLocation>> getDrivers(@Query("id") int id, @Query("radio") int radio,
                                                     @Query("latitude") double latitude, @Query("longitude") double longitude);

    @PUT("drivers/{driverId}/location")
    Call<String> updateLocation(@Path("driverId") int driverId, @Body Location location);

}
