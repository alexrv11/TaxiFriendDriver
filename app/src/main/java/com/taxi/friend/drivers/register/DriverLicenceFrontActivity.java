package com.taxi.friend.drivers.register;

import android.os.Bundle;

import com.taxi.friend.drivers.TaxiGlobalInfo;

public class DriverLicenceFrontActivity extends BaseRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( DriverLicenseBackActivity.class, TaxiGlobalInfo.LICENSE_FRONT_PHOTO);
    }

}
