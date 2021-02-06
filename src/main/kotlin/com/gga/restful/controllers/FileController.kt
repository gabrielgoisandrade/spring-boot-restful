package com.gga.restful.controllers

import com.gga.restful.models.dto.FileDTO
import com.gga.restful.services.FileStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@Tag(name = "File endpoint", description = "Operations from resource 'File'")
@RestController
@RequestMapping("/file")
class FileController {

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    private val logger: Logger? = LoggerFactory.getLogger(this::class.java)

    @Operation(description = "Upload file")
    @PostMapping("/uploadFile", consumes = ["multipart/form-data"])
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<FileDTO> {
        val fileName: String = this.fileStorageService.storeFile(file)

        val fileDownloadURI: String =
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/downloadFile/")
                .path(fileName).toUriString()

        FileDTO(file.name, fileDownloadURI, file.contentType!!, file.size).also {
            return ok(it)
        }
    }

    @Operation(description = "Upload files")
    @PostMapping("/uploadFiles", consumes = ["multipart/form-data"])
    fun uploadFiles(@RequestParam("files") files: List<MultipartFile>): ResponseEntity<List<FileDTO>> =
        files.stream().map {
            val fileName: String = this.fileStorageService.storeFile(it)

            val fileDownloadURI: String =
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/downloadFile/")
                    .path(fileName).toUriString()

            FileDTO(it.name, fileDownloadURI, it.contentType!!, it.size)
        }.collect(Collectors.toList()).run { ok(this) }

    @Operation(description = "Download file")
    @GetMapping("/downloadFile/{fileName:.+}") //diz que pode receber uma extensão
    fun downloadFile(
        @PathVariable("fileName") fileName: String,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {
        val resource: Resource = this.fileStorageService.loadFileAsResource(fileName)

        var contentType: String? = null

        runCatching {
            contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        }.onFailure {
            this.logger?.info("Could not determine file type")
        }

        if (contentType == null) {
            contentType = "application/octet-stream"
        }

        return ok()
            .contentType(MediaType.parseMediaType(contentType!!))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${resource.filename}\"")
            .body(resource)

    }

}