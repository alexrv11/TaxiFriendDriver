package com.taxi.friend.drivers.register;

import android.os.Bundle;

import com.taxi.friend.drivers.TaxiGlobalInfo;

public class CarFrontPictureActivity extends BaseRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( CarSidePictureActivity.class, TaxiGlobalInfo.CAR_FRONT_PHOTO);
    }
}
