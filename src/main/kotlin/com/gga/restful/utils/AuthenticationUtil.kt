package com.gga.restful.utils

import com.gga.restful.errors.exceptions.AuthException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AuthenticationUtil private constructor() {
    companion object {

        @JvmStatic
        @Throws(AuthException::class)
        fun UserDetails.verify(): UserDetails =
            if (this.username == "noUsername") throw AuthException("Username/email not found.") else this

        @JvmStatic
        @Throws(AuthException::class)
        fun verifyPassword(encoder: BCryptPasswordEncoder, password: String, toCompare: String) {
            if (!encoder.matches(password, toCompare)) throw AuthException("Invalid password.")
        }
    }
}