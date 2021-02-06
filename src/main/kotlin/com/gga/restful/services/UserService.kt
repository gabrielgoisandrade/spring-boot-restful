package com.gga.restful.services

import com.gga.restful.errors.exceptions.PersonNotFoundException
import com.gga.restful.models.UserModel
import com.gga.restful.models.dto.UserDTO
import com.gga.restful.repositories.UserRepository
import com.gga.restful.utils.ConverterUtil.Companion.parseObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

    fun findById(id: Long): UserDTO = this.repository.findById(id).orElseThrow {
        PersonNotFoundException("Person not found.")
    }.run {
        parseObject(this, UserDTO::class.java)
    }

    fun findAll(pageable: Pageable): Page<UserDTO> =
        this.repository.findAll(pageable).run {
            this.map { parseObject(it, UserDTO::class.java) }
        }

    @Transactional
    fun createPerson(userDTO: UserDTO): UserDTO =
        parseObject(userDTO, UserModel::class.java).apply { this.password = encoder.encode(this.password) }.run {
            parseObject(repository.save(this), UserDTO::class.java)
        }

    @Transactional
    fun updatePerson(userDTO: UserDTO): UserDTO {

        // Houve outra chamada do findById porque a conexão com o database fecha a cada vez que um comando é executado.
        val currentUser: UserModel = this.repository.findById(userDTO.personId).orElseThrow {
            PersonNotFoundException("Person not found.")
        }

        val newUser: UserModel = currentUser.apply {
            with(userDTO) {
                this@apply.name = this.name
                this@apply.lastName = this.lastName
                this@apply.email = this.email
                this@apply.email = this.password
            }
        }

        this.repository.save(newUser).run {
            return parseObject(this, UserDTO::class.java)
        }

    }

    @Transactional
    fun deletePerson(id: Long): Unit = this.findById(id).run { this@UserService.repository.deleteById(this.personId) }

}