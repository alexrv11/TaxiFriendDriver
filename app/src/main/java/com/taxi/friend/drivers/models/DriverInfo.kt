package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonProperty


data class DriverInfo(
        var id: String = "",
        var name: String = "",
        var phone: String = "",
        var credit: Int = 0,
        var status: String = "",
        @JsonProperty("car_identity") var carIdentity: String = ""
)