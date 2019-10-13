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
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Builder
@NoArgsConstructor
class ResponseWrapper<T> {

    var errors: ErrorMessage? = null
        set(errors) {
            field = this.errors
        }
    var result: T? = null
        set(result) {
            field = this.result
        }
}
