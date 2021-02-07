package com.gga.restful.models.dto

import org.springframework.hateoas.RepresentationModel
import java.io.Serializable

data class FileDTO @JvmOverloads constructor(
    var fileName: String = "",
    var fileType: String = "",
    var size: Long = 0L
) : Serializable, RepresentationModel<FileDTO>()
