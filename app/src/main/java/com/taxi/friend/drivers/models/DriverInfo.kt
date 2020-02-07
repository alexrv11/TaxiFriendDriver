package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
class DriverInfo(id: String = "", name: String = "", phone: String = "", credit: Int = 0, status: String = "", carIdentity: String = "") {

    val id = id
    val name = name
    val phone = phone
    val credit = credit
    val status = status

    @JsonProperty("car_identity")
    val carIdentity = carIdentity
}