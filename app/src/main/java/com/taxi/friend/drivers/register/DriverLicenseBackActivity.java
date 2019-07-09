package com.taxi.friend.drivers.register;

import android.os.Bundle;

import com.taxi.friend.drivers.TaxiGlobalInfo;

public class DriverLicenseBackActivity extends BaseRegisterActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( CarFrontPictureActivity.class, TaxiGlobalInfo.LICENSE_BACK_PHOTO);
    }
}
