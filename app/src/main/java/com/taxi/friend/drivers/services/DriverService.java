package com.taxi.friend.drivers.services;

import android.location.Location;

import com.taxi.friend.drivers.models.DriverLocation;
import com.taxi.friend.drivers.models.ResponseWrapper;
import com.taxi.friend.drivers.repository.DriverRepository;
import com.taxi.friend.drivers.repository.RestClient;


import retrofit2.Call;

public class DriverService {

    public Call<ResponseWrapper<DriverLocation>> getDriverMates(int driverId) throws Exception {

        DriverRepository service = RestClient.createRestClient().create(DriverRepository.class);
        Call<ResponseWrapper<DriverLocation>> result = service.getDrivers(driverId, 1, -34.634713, -58.436193);
        return result;
    }

    public Call<String> updateLocation(int driverId, Location location){
        DriverRepository service = RestClient.createRestClient().create(DriverRepository.class);

        return service.updateLocation(driverId, new com.taxi.friend.drivers.models.Location(location.getLatitude(), location.getLongitude()));
    }
}
