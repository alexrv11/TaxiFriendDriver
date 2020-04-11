package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Qr (
    var code: String = "",
    var status: String = "",
    var credit: Double = 0.0,
    @JsonProperty("driver_id") var driverId: String = ""
)
