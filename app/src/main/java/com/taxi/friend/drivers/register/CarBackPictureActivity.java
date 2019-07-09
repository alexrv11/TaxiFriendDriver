package com.taxi.friend.drivers.register;

import android.os.Bundle;

import com.taxi.friend.drivers.TaxiGlobalInfo;

public class CarBackPictureActivity extends BaseRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( RegisterDriverAccountActivity.class, TaxiGlobalInfo.CAR_BACK_PHOTO);
    }
}
