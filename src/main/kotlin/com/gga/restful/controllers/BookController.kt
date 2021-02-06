package com.gga.restful.controllers

import com.gga.restful.models.dto.BookDTO
import com.gga.restful.services.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as Body

@Tag(name = "Book endpoint", description = "Operations from resource 'Book'")
@RestController
@RequestMapping("/book")
class BookController {

    @Autowired
    private lateinit var service: BookService

    @Operation(
        description = "Get all recorded books",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ]
    )
    @GetMapping(produces = ["application/json", "application/xml", "application/x-yaml"])
    fun findAll(): ResponseEntity<List<BookDTO>> {
        val body: List<BookDTO> = this.service.findAll()

        body.forEach { it.add(linkTo(methodOn(this::class.java).findById(it.bookId)).withSelfRel()) }

        return ok(body)
    }

    @Operation(
        description = "Get recorded book by its ID",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Book not found", responseCode = "404"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ]
    )
    @GetMapping("/{id}", produces = ["application/json", "application/xml", "application/x-yaml"])
    fun findById(@PathVariable("id") id: Long): ResponseEntity<BookDTO> {
        val bookDTO: BookDTO = this.service.findById(id)

        bookDTO.add(linkTo(methodOn(this::class.java).findAll()).withRel("All books"))

        return ok(bookDTO)
    }

    @Operation(
        description = "Record a new book",
        responses = [ApiResponse(description = "Created", responseCode = "201")],
        requestBody = Body(description = "Book's body")
    )
    @PostMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"], // produz (retorna) json, xml e yaml
        consumes = ["application/json", "application/xml", "application/x-yaml"] // consome (recebe) json, xml e yaml
    )
    fun createBook(@RequestBody BookModel: BookDTO): ResponseEntity<BookDTO> {
        val bookDTO: BookDTO = this.service.createBook(BookModel)

        bookDTO.add(linkTo(methodOn(this::class.java).findById(bookDTO.bookId)).withSelfRel())

        return status(CREATED).body(bookDTO)
    }

    @Operation(
        description = "Update a existing book",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ],
        requestBody = Body(description = "Book's body")
    )
    @PutMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    fun updateBook(@RequestBody BookModel: BookDTO): ResponseEntity<BookDTO> {
        val bookDTO: BookDTO = this.service.updateBook(BookModel)

        bookDTO.add(linkTo(methodOn(this::class.java).findById(bookDTO.bookId)).withSelfRel())

        return ok(bookDTO)
    }

    @Operation(
        description = "Delete a existing book by its ID",
        responses = [
            ApiResponse(description = "No Content", responseCode = "204"),
            ApiResponse(description = "Access denied", responseCode = "403")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable("id") id: Long): ResponseEntity<Void> {
        this.service.deleteBook(id)
        return noContent().build()
    }

}