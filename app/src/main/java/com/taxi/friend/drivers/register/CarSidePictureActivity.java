package com.taxi.friend.drivers.register;

import android.os.Bundle;
import com.taxi.friend.drivers.TaxiGlobalInfo;

public class CarSidePictureActivity extends BaseRegisterActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( CarBackPictureActivity.class, TaxiGlobalInfo.CAR_SIDE_PHOTO);
    }
}
