package com.taxi.friend.drivers.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Qr {
    var code: String? = null
        set(code) {
            field = this.code
        }
    var status: String? = null
        set(status) {
            field = this.status
        }
    var credit: Double = 0.toDouble()
        set(credit) {
            field = this.credit
        }
}
