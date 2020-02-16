package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Order(id: String = "", driverId: String = "", latitude: Double = 0.0, longitude: Double = 0.0, status: String = "") {
    val id = id

    @JsonProperty("driver_id")
    val driverId = driverId

    val latitude = latitude
    val longitude = longitude
    val status = status
}