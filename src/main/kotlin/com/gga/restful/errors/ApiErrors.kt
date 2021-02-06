package com.gga.restful.errors

import java.io.Serializable
import java.util.*

data class ApiErrors @JvmOverloads constructor(
    val message: String,
    val details: String,
    val timestamp: Date = Date()
) : Serializable
