package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonProperty

data class LoadCredit(
        @JsonProperty("driver_id") var driverId: String = ""
)
