package com.gga.restful.models.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable
import java.util.*

data class BookDTO @JvmOverloads constructor(
    @Mapping("id")
    @JsonProperty("id")
    val bookId: Long = 0L,

    var author: String = "",

    var title: String = "",

    var price: Double = 0.0,

    @JsonFormat(pattern = "yyyy-MM-dd")
    var launchDate: Date = Date()
) : Serializable, RepresentationModel<BookDTO>()
