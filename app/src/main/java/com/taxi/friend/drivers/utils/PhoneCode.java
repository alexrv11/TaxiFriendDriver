package com.taxi.friend.drivers.utils;

public class PhoneCode {

    public static String getNumber(String phone) {

        if (phone.equals("34034426")) {
            return "+54911" + phone;
        } else if(!phone.startsWith("+591")) {
            return "+591" + phone;
        }

        return phone;
    }
}
