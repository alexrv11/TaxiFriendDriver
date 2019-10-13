package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class DriverLocation {
    var id: Int = 0
        set(id) {
            field = this.id
        }
    var name: String? = null
        set(name) {
            field = this.name
        }
    var latitude: Double? = null
        set(Latitude) {
            field = latitude
        }
    var longitude: Double = 0.toDouble()
        set(longitude) {
            field = this.longitude
        }
    var status: String? = null
        set(status) {
            field = this.status
        }
    var direction: Int = 0
        set(direction) {
            field = this.direction
        }
}
