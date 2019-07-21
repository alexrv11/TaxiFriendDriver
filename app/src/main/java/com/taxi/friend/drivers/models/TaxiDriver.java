package com.taxi.friend.drivers.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TaxiDriver {

    private String id;

    private String name;

    private String carFrontPhoto;

    private String carBackPhoto;

    private String licenseFrontPhoto;

    private String licenseBackPhoto;

    private String carSidePhoto;


    private String phone;

    private String password;

    private String carIdentity;

    private double credit;

    public TaxiDriver(String name, String carFrontPhoto, String carBackPhoto, String carSidePhoto, String licenseFrontPhoto,
                      String licenseBackPhoto, String phone, String carIdentity, String password) {
        this.name = name;
        this.carFrontPhoto = carFrontPhoto;
        this.carBackPhoto = carBackPhoto;
        this.carSidePhoto =  carSidePhoto;
        this.licenseFrontPhoto = licenseFrontPhoto;
        this.licenseBackPhoto = licenseBackPhoto;
        this.phone = phone;
        this.password = password;
        this.carIdentity = carIdentity;
        this.credit = 0;
    }

}
