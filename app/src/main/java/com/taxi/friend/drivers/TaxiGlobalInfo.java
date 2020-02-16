package com.taxi.friend.drivers;

import android.graphics.Bitmap;

import com.taxi.friend.drivers.models.DriverInfo;
import com.taxi.friend.drivers.models.DriverLocation;

import com.taxi.friend.drivers.view.models.MenuMainUserViewModel;

import java.util.HashMap;

public class TaxiGlobalInfo {

    public static String LICENSE_BACK_PHOTO = "LICENSE_BACK_PHOTO";
    public static String CAR_BACK_PHOTO = "CAR_BACK_PHOTO";
    public static String CAR_FRONT_PHOTO = "CAR_FRONT_PHOTO";
    public static String CAR_SIDE_PHOTO = "CAR_SIDE_PHOTO";
    public static String LICENSE_FRONT_PHOTO = "LICENSE_FRONT_PHOTO";
    public static HashMap<String, String> photoMap = new HashMap<>();
    public static HashMap<String, Bitmap> photoBitmap = new HashMap<>();
    public static DriverInfo taxiDriver;
    public static MenuMainUserViewModel mainViewModel;
    public static String DriverId="6";


    static boolean isShowOrderDialog = false;
}
