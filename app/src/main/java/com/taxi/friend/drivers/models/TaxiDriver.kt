package com.taxi.friend.drivers.models


import com.fasterxml.jackson.annotation.JsonProperty


data class TaxiDriver(
        var name: String = "",
        @JsonProperty("front_car_photo") var carFrontPhoto: String = "",
        @JsonProperty("back_car_photo") var carBackPhoto: String = "",
        @JsonProperty("side_car_photo") var carSidePhoto: String = "",
        @JsonProperty("front_license_photo") var licenseFrontPhoto: String = "",
        @JsonProperty("back_license_photo") var licenseBackPhoto: String = "",
        var phone: String = "",
        @JsonProperty("car_identity") var carIdentity: String = ""
)