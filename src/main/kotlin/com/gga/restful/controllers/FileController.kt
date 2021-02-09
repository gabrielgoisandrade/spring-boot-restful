package com.gga.restful.controllers

import com.gga.restful.models.dto.FileDTO
import com.gga.restful.services.FileService
import com.gga.restful.utils.FileUtil.Companion.linkToDownLoad
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@Tag(name = "File endpoint", description = "Operations from resource 'File'")
@RestController
@RequestMapping("/file")
class FileController {

    @Autowired
    private lateinit var fileService: FileService

    /**
     * # Upload de arquivo
     *
     * Permite que o usuário envie um arquivo (nesse caso, qualquer tipo) durante a requisição.
     *
     * HATEOAS implementado.
     *
     * @param file arquivo a ser salvo
     * @see uploadFiles
     * @see downloadFile
     * @see com.gga.restful.services.FileService.storeFile
     * @author Gabriel Gois Andrade
     * */
    @Operation(
        description = "Upload file",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Parameter 'file' empty", responseCode = "415")
        ],
        security = [SecurityRequirement(name = "Access token")]
    )
    @PostMapping("/upload-file", consumes = ["multipart/form-data"])
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<FileDTO> =
        this.fileService.storeFile(file).run {
            this linkToDownLoad this.fileName

            ok(this)
        }

    /**
     * # Upload de múltiplos arquivos
     *
     * Permite que o usuário envie vários arquivos (nesse caso, qualquer tipo) durante a requisição.
     *
     * HATEOAS implementado.
     *
     * @param files arquivos a serem salvos
     * @see downloadFile
     * @see com.gga.restful.services.FileService.storeFile
     * @author Gabriel Gois Andrade
     * */
    @Operation(
        description = "Upload files",
        responses = [
            ApiResponse(description = "File stored", responseCode = "200"),
            ApiResponse(description = "Needs access token", responseCode = "403"),
            ApiResponse(description = "Parameter 'files' empty", responseCode = "415"),
            ApiResponse(description = "Fail to store file", responseCode = "500")
        ],
        security = [SecurityRequirement(name = "Access token")]
    )
    @PostMapping("/upload-files", consumes = ["multipart/form-data"])
    fun uploadFiles(@RequestParam("files") files: List<MultipartFile>): ResponseEntity<List<FileDTO>> =
        files.stream().map { this.fileService.storeFile(it) }.collect(Collectors.toList()).run {
            this.forEach { it linkToDownLoad it.fileName }

            ok(this)
        }

    /**
     * # Download de arquivos
     *
     * Busca os arquivos enviados por [uploadFile] ou [uploadFiles] para que possa ser baixado.
     *
     * @param fileName arquivo a ser buscado
     * @see uploadFiles
     * @see uploadFile
     * @see com.gga.restful.services.FileService.findFile
     * @author Gabriel Gois Andrade
     * */
    @Operation(
        description = "Download file",
        responses = [
            ApiResponse(description = "File found", responseCode = "200"),
            ApiResponse(description = "Needs access token", responseCode = "403"),
            ApiResponse(description = "File not found", responseCode = "404")
        ],
        security = [SecurityRequirement(name = "Access token")]
    )
    @GetMapping("/download-file/{fileName:.+}") //diz que pode receber uma extensão
    fun downloadFile(
        @PathVariable("fileName") fileName: String,
        request: HttpServletRequest? = null
    ): ResponseEntity<Resource> {
        var contentType: String? = null

        val resource: Resource = this.fileService.findFile(fileName)

        runCatching { contentType = request!!.servletContext.getMimeType(resource.file.absolutePath) }

        // seta o content-type como tipo genérico para arquivos caso não ache o tipo correto
        if (contentType == null) contentType = "application/octet-stream"

        return ok()
            .contentType(MediaType.parseMediaType(contentType!!))
            .header(
                CONTENT_DISPOSITION,
                "attachment; filename=\"${resource.filename}\""
            ) // configurando visualização dos arquivos
            .body(resource)
    }

}