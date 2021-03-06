package com.gga.restful.controllers

import com.gga.restful.errors.ApiErrors
import com.gga.restful.models.dto.LoginDTO
import com.gga.restful.services.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as Body

@Tag(name = "Authentication controller", description = "User authentication")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var service: AuthenticationService

    @Operation(
        description = "Authentication",
        responses = [
            ApiResponse(description = "User authenticated", responseCode = "202"),
            ApiResponse(
                description = "Invalid credentials", responseCode = "401",
                content = [Content(schema = Schema(implementation = ApiErrors::class))]
            )],
        requestBody = Body(description = "User's credentials")
    )
    @PostMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    fun auth(@RequestBody loginDTO: LoginDTO): ResponseEntity<HashMap<String, Any>> =
        this.service.authenticate(loginDTO).run { status(ACCEPTED).body(this) }

}