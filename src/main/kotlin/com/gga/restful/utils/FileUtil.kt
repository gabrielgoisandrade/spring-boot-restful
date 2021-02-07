package com.gga.restful.utils

import com.gga.restful.controllers.FileController
import com.gga.restful.models.dto.FileDTO
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi

class FileUtil private constructor() {
    companion object {

        @JvmStatic
        fun getPath(path: String, fileName: String): Path = Paths.get(path + fileName)

        @JvmStatic
        @ExperimentalPathApi
        infix fun FileDTO.linkToDownLoad(fileName: String): FileDTO =
            this.add(linkTo(methodOn(FileController::class.java).downloadFile(fileName)).withRel("Download file"))
    }

}