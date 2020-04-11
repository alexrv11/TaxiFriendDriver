package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
class DriverLocation(
        var id: String = "",
        var name: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var status: String = "",
        var direction: Int = 0
)
