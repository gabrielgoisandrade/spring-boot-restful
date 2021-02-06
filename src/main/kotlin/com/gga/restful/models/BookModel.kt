package com.gga.restful.models

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BOOK")
data class BookModel @JvmOverloads constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    val id: Long = 0L,

    @Column(name = "AUTHOR", nullable = false, length = 80)
    var author: String,

    @Column(name = "TITLE", nullable = false, length = 80)
    var title: String,

    @Column(name = "PRICE", nullable = false, precision = 2)
    var price: Double,

    @Column(name = "LAUNCH_DATE", nullable = false)
    @Temporal(TemporalType.DATE) // para datas
    var launchDate: Date
) : Serializable