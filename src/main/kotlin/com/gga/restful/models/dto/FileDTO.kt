package com.gga.restful.models.dto

import java.io.Serializable

data class FileDTO @JvmOverloads constructor(
    var fileName: String = "",
    var fileDownloadURI: String = "",
    var fileType: String = "",
    var size: Long = 0L
) : Serializable
