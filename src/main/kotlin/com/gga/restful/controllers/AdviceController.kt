package com.gga.restful.controllers

import com.gga.restful.errors.ApiErrors
import com.gga.restful.errors.exceptions.*
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class AdviceController {

    @ExceptionHandler(PersonNotFoundException::class)
    fun handler(ex: PersonNotFoundException, request: WebRequest): ResponseEntity<ApiErrors> =
        ApiErrors(ex.message!!, request.getDescription(false)).run { status(NOT_FOUND).body(this) }

    @ExceptionHandler(BookNotFoundException::class)
    fun handler(ex: BookNotFoundException, request: WebRequest): ResponseEntity<ApiErrors> =
        ApiErrors(ex.message!!, request.getDescription(false)).run { status(NOT_FOUND).body(this) }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handler(ex: UsernameNotFoundException, request: WebRequest): ResponseEntity<ApiErrors> =
        ApiErrors(ex.message!!, request.getDescription(false)).run { status(NOT_FOUND).body(this) }

    @ExceptionHandler(FileNotFoundException::class)
    fun handler(ex: FileNotFoundException, request: WebRequest): ResponseEntity<ApiErrors> =
        ApiErrors(ex.message!!, request.getDescription(false)).run { status(NOT_FOUND).body(this) }

    @ExceptionHandler(AuthException::class)
    fun handler(ex: AuthException, request: WebRequest): ResponseEntity<ApiErrors> =
        ApiErrors(ex.message!!, request.getDescription(false)).run { status(UNAUTHORIZED).body(this) }

    @ExceptionHandler(FileException::class)
    fun handler(ex: FileException, request: WebRequest): ResponseEntity<ApiErrors> =
        ApiErrors(ex.message!!, request.getDescription(false)).run { status(INTERNAL_SERVER_ERROR).body(this) }

}