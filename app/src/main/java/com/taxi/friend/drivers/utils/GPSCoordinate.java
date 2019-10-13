package com.taxi.friend.drivers.utils;

import android.util.Log;

import com.taxi.friend.drivers.constants.Constants;


public class GPSCoordinate {



    public static double distanceInMeterBetweenEarthCoordinates(double lat1, double long1, double lat2, double long2) {

        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(long2-long1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double result = (Constants.INSTANCE.getRADIO_EART() * c) * 1000;
        Log.i("DistanceDriver", String.format("Distance driver %f", result));
        return result;
    }
}

