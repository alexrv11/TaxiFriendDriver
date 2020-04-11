package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Order(
        var id: String = "",
        @JsonProperty("driver_id") var driverId: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var status: String = ""
)