package com.gga.restful.models

import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY
import javax.validation.constraints.Email

@Entity
@Table(name = "USER")
data class UserModel @JvmOverloads constructor(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "USER_ID")
    val id: Long = 0L,

    @Column(name = "NAME", nullable = false, length = 30)
    var name: String,

    @Column(name = "LAST_NAME", nullable = false, length = 80)
    var lastName: String,

    @Email
    @Column(name = "EMAIL", nullable = false, length = 100)
    var email: String,

    @Column(name = "PASSWORD", nullable = false, length = 100)
    var password: String

) : Serializable
