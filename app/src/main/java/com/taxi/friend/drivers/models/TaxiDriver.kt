package com.taxi.friend.drivers.models


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
class TaxiDriver {

    var id: String? = null
        set(id) {
            field = this.id
        }

    private val name: String

    private val carFrontPhoto: String

    private val carBackPhoto: String

    private val licenseFrontPhoto: String

    private val licenseBackPhoto: String

    private val carSidePhoto: String


    private val phone: String

    private val password: String

    private val carIdentity: String

    private val credit: Double

    constructor(name: String, carFrontPhoto: String, carBackPhoto: String, carSidePhoto: String, licenseFrontPhoto: String,
                licenseBackPhoto: String, phone: String, carIdentity: String, password: String) {
        this.name = name

        @JsonProperty("front_car_photo")
        this.carFrontPhoto = carFrontPhoto

        @JsonProperty("back_car_photo")
        this.carBackPhoto = carBackPhoto

        @JsonProperty("side_car_photo")
        this.carSidePhoto = carSidePhoto

        @JsonProperty("front_license_photo")
        this.licenseFrontPhoto = licenseFrontPhoto

        @JsonProperty("back_license_photo")
        this.licenseBackPhoto = licenseBackPhoto
        this.phone = phone
        this.password = password
        @JsonProperty("car_identity")
        this.carIdentity = carIdentity
        this.credit = 0.0
    }

}
