package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

class LoadCredit(driverId: String = "") {
    @JsonProperty("driver_id")
    val driverId = driverId

}
