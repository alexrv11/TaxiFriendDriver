package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Credit {
    var driverId: String? = null
        set(driverId) {
            field = this.driverId
        }
    var credit: Double = 0.toDouble()
        set(credit) {
            field = this.credit
        }
}
