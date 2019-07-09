package com.taxi.friend.drivers;

import android.graphics.Bitmap;

import java.util.HashMap;

public class TaxiGlobalInfo {

    public static String LICENSE_BACK_PHOTO = "LICENSE_BACK_PHOTO";
    public static String CAR_BACK_PHOTO = "CAR_BACK_PHOTO";
    public static String CAR_FRONT_PHOTO = "CAR_FRONT_PHOTO";
    public static String CAR_SIDE_PHOTO = "CAR_SIDE_PHOTO";
    public static String LICENSE_FRONT_PHOTO = "LICENSE_FRONT_PHOTO";
    public static HashMap<String, String> photoMap = new HashMap<>();
    public static HashMap<String, Bitmap> photoBitmap = new HashMap<>();
}
