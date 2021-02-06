package com.gga.restful.models.dto

import java.io.Serializable

data class LoginDTO(
    var username: String = "",
    var password: String = "",
) : Serializable