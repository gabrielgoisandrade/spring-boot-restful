package com.gga.restful.controllers

import com.gga.restful.models.dto.UserDTO
import com.gga.restful.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as Body

/**
 * O controller agora é capaz de produzir tanto JSON quanto XML e também consumir os mesmos
 *
 * produce -> Tipo de dado retornado (response)
 *
 * consume -> Tipo de dado que pode ser consumido (request)
 *
 * @author Gabriel Gois Andrade
 * */
@Tag(name = "User endpoint", description = "Operations from resource 'User'")
@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var service: UserService

    @Operation(
        description = "Get all recorded users",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ]
    )
    @GetMapping(produces = ["application/json", "application/xml", "application/x-yaml"])
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") limit: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
        assembler: PagedResourcesAssembler<UserDTO>
    ): ResponseEntity<PagedModel<EntityModel<UserDTO>>> {

        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC))

        val users: Page<UserDTO> = this.service.findAll(pageable)
        users.forEach { it.add(linkTo(methodOn(this::class.java).findById(it.personId)).withSelfRel()) }

        return ok(assembler!!.toModel(users))
    }

    @Operation(
        description = "Get recorded user by its ID",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "User not found", responseCode = "404"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ]
    )
    @GetMapping("/{id}", produces = ["application/json", "application/xml", "application/x-yaml"])
    fun findById(@PathVariable("id") id: Long): ResponseEntity<UserDTO> {
        val userDTO: UserDTO = this.service.findById(id)

        // adicionando link auto relacionado
        userDTO.add(linkTo(methodOn(this::class.java).findById(id)).withRel("All people"))

        return ok(userDTO)
    }

    @Operation(
        description = "Record a new user",
        responses = [ApiResponse(description = "Created", responseCode = "201")],
        requestBody = Body(description = "User's body")
    )
    @PostMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"], // produz (retorna) json, xml e yaml
        consumes = ["application/json", "application/xml", "application/x-yaml"] // consome (recebe) json, xml e yaml
    )
    fun createPerson(@RequestBody userModel: UserDTO): ResponseEntity<UserDTO> {
        val userDTO: UserDTO = this.service.createPerson(userModel)

        userDTO.add(linkTo(methodOn(this::class.java).findById(userDTO.personId)).withSelfRel())

        return status(CREATED).body(userDTO)
    }

    @Operation(
        description = "Update a existing user",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ],
        requestBody = Body(description = "User's body")
    )
    @PutMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    fun updatePerson(@RequestBody userModel: UserDTO): ResponseEntity<UserDTO> {
        val userDTO: UserDTO = this.service.updatePerson(userModel)

        userDTO.add(linkTo(methodOn(this::class.java).findById(userDTO.personId)).withSelfRel())

        return ok(userDTO)
    }

    @Operation(
        description = "Delete a existing user by its ID",
        responses = [
            ApiResponse(description = "No Content", responseCode = "204"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ]
    )
    @DeleteMapping("/{id}")
    fun deletePerson(@PathVariable("id") id: Long): ResponseEntity<Void> {
        this.service.deletePerson(id)
        return noContent().build()
    }

}