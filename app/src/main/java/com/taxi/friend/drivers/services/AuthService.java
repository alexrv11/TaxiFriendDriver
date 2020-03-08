package com.taxi.friend.drivers.services;

import android.util.Log;

import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.models.DriverInfo;
import com.taxi.friend.drivers.models.TaxiDriver;
import com.taxi.friend.drivers.utils.ImageHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    public void registerUserInformation(String name, String phone, String carIdentity) {


        String carFrontImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.CAR_FRONT_PHOTO));
        String carBackImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.CAR_BACK_PHOTO));
        String carSideImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.CAR_SIDE_PHOTO));
        String licenseFrontImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.LICENSE_FRONT_PHOTO));
        String licenseBackImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.LICENSE_BACK_PHOTO));

        TaxiDriver driver = new TaxiDriver(name, carFrontImage, carBackImage, carSideImage, licenseFrontImage, licenseBackImage,
                phone, carIdentity);


        DriverService service = new DriverService();
        Call<DriverInfo> callRequest = service.createDriver(driver);

        try {

            callRequest.enqueue(new Callback<DriverInfo>() {
                @Override
                public void onResponse(Call<DriverInfo> call, Response<DriverInfo> response) {
                    int code = response.code();
                    if (code != 200) {
                        return;
                    }

                    TaxiGlobalInfo.taxiDriver = response.body();
                }

                @Override
                public void onFailure(Call<DriverInfo> call, Throwable t) {
                    Log.e("ErrorServer", t.getMessage());
                }
            });
        }catch (Exception e ){
            Log.e("ErrorCreateDriver", e.getMessage());
            e.printStackTrace();
        }
    }
}
