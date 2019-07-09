package com.taxi.friend.drivers.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.TaxiGlobalInfo;

public class CarSidePictureActivity extends BaseRegisterActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( CarBackPictureActivity.class, TaxiGlobalInfo.CAR_SIDE_PHOTO);
    }
}
