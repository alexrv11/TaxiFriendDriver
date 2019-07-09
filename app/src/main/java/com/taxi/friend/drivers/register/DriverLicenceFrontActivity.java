package com.taxi.friend.drivers.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.utils.PhotoManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DriverLicenceFrontActivity extends BaseRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.initialize( DriverLicenseBackActivity.class, TaxiGlobalInfo.LICENSE_FRONT_PHOTO);
    }

}
