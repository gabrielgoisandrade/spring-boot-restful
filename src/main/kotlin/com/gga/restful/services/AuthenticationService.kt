package com.gga.restful.services

import com.gga.restful.models.UserModel
import com.gga.restful.models.dto.LoginDTO
import com.gga.restful.repositories.UserRepository
import com.gga.restful.security.JwtTokenProvider
import com.gga.restful.utils.AuthenticationUtil.Companion.verify
import com.gga.restful.utils.AuthenticationUtil.Companion.verifyPassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService : UserDetailsService {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var provider: JwtTokenProvider

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

    fun authenticate(loginDTO: LoginDTO): HashMap<String, Any> =
        this.loadUserByUsername(loginDTO.username).verify().run {
            verifyPassword(encoder, loginDTO.password, this.password)

            hashMapOf("token" to provider.createToken(loginDTO.username))
        }

    /**
     * # Retorna o usuário convertido em UserDetails
     *
     * UserDetails *não* retorna null, nem pode ter entradas nulas ou " " (string vazia), então caso o objeto a ser
     * convertido venha como null, um valor padrão é informado (utilizando o null safe do kotlin).
     * Esse valor é lido durante o Filter (que ocorre antes da requisição chegar no controller), ali as devidas
     * ações serão tomadas.
     *
     * @param username usuário a ser buscado para autenticação
     * @see com.gga.restful.security.JwtTokenFilter
     * @author Gabriel Gois Andrade
     * */
    override fun loadUserByUsername(username: String?): UserDetails {
        val user: UserModel? = this.repository.findByEmail(username!!)

        return with(user) {
            User.builder()
                .username(this?.email ?: "noUsername")
                .password(this?.password ?: "noPassword")
                .roles("")
                .build()
        }
    }

}
