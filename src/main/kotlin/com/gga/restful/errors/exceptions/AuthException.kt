package com.gga.restful.errors.exceptions

import org.springframework.security.core.AuthenticationException

class AuthException(message: String) : AuthenticationException(message)
