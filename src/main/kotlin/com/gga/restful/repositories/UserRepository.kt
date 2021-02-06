package com.gga.restful.repositories

import com.gga.restful.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserModel, Long> {

    fun findByEmail(@Param("email") email: String): UserModel?

}