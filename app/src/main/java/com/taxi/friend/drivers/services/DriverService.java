package com.taxi.friend.drivers.services;

import android.location.Location;
import android.util.Log;

import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.models.DriverInfo;
import com.taxi.friend.drivers.models.DriverLocation;
import com.taxi.friend.drivers.models.ResponseWrapper;
import com.taxi.friend.drivers.models.TaxiDriver;
import com.taxi.friend.drivers.models.User;
import com.taxi.friend.drivers.repository.DriverRepository;
import com.taxi.friend.drivers.repository.RestClient;


import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverService {

    public Call<ResponseWrapper<List<DriverLocation>>> getDriverLocations(int radio, double latitude, double longitude) throws Exception {

        DriverRepository service = RestClient.createRestClient().create(DriverRepository.class);
        Call<ResponseWrapper<List<DriverLocation>>> result = service.getDriverLocations( radio, latitude, longitude);
        return result;
    }

    public Call<String> updateLocation(String driverId, Location location){
        DriverRepository service = RestClient.createRestClient().create(DriverRepository.class);

        return service.updateLocation(driverId, new com.taxi.friend.drivers.models.Location(location.getLatitude(), location.getLongitude()));
    }

    public Call<DriverInfo> createDriver(TaxiDriver taxiDriver){
        DriverRepository service = RestClient.createRestClient().create(DriverRepository.class);

        return service.createDriver(taxiDriver);
    }

    public void getDriver(String driverId){
        DriverRepository repository = RestClient.createRestClient().create(DriverRepository.class);

        Call<ResponseWrapper<User>> callUser = repository.getDriver(driverId);
        callUser.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if(response.code() == HttpURLConnection.HTTP_OK){
                    User user = response.body().getResult();
                    TaxiGlobalInfo.mainViewModel.setUser(user);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.e("ErrorDriveInfo", t.getMessage(), t);
            }
        });
        return ;
    }
}
