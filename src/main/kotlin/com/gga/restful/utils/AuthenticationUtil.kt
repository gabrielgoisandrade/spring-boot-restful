package com.gga.restful.utils

import com.gga.restful.errors.exceptions.AuthException
import org.springframework.security.core.userdetails.UserDetails

class AuthenticationUtil private constructor() {
    companion object {
        @JvmStatic
        fun UserDetails.verify(): UserDetails =
            if (this.username == "noUsername") throw AuthException("Username/email not found.") else this
    }
}