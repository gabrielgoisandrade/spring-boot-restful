package com.gga.restful.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.gga.restful.errors.ApiErrors
import com.gga.restful.errors.exceptions.AuthException
import com.gga.restful.services.AuthenticationService
import com.gga.restful.utils.AuthenticationUtil.Companion.verify
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtTokenFilter(
    private val tokenProvider: JwtTokenProvider,
    private val authenticationService: AuthenticationService
) : OncePerRequestFilter() {

    /**
     * # Filtro de requisição
     *
     * Esse filtro vai *interceptar* todas as requisições e suas ações irão variar de acordo com o que foi definido nas
     * [configurações de segurança][com.gga.restful.config.SecurityConfig] para cada endpoint.
     *
     * Esse filtro é executado antes do controller, já que utiliza o Servlet e os Controller utilizam o Dispatcher.
     * Por esse motivo, quando uma exception é gerada, mesmo esteja mapeada no AdviceController, nada acontecerá, pois
     * o Dispatcher não entrou em execução.
     *
     * Além de realizar a interceptação durante as requisições, esse método também fica responsável por tratar as
     * possíveis exceptions que virão. Para tratá-las, deve-se retornar o erro "manualmente" em forma de JSON, como
     * está sendo feito no [raiseException][com.gga.restful.security.jwt.JwtTokenFilter.raiseException].
     *
     * @see com.gga.restful.config.SecurityConfig
     * @author Gabriel Gois Andrade
     * */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        this.getTokenFromHeader(request).runCatching {
            if (this != null) {
                val username: String = tokenProvider.getUsername(this)

                val user: UserDetails = authenticationService.loadUserByUsername(username).verify()

                UsernamePasswordAuthenticationToken(user, null, user.authorities).also {
                    SecurityContextHolder.getContext().authentication = it
                }
            }

            chain.doFilter(request, response)

        }.onFailure {
            when (it) {
                is JwtException -> {
                    this.raiseException(request, response, it.message!!)
                    return
                }
                is AuthException -> {
                    this.raiseException(request, response, it.message!!)
                    return
                }
            }
        }
    }

    private fun getTokenFromHeader(request: HttpServletRequest): String? {
        val headerValue: String? = request.getHeader(AUTHORIZATION)

        return if (headerValue != null && headerValue.startsWith("Bearer "))
            headerValue.split("Bearer ")[1]
        else
            null
    }

    /**
     * Trata a exception retornando um JSON, assim como o AdviceController.
     *
     * @param request informações da requisição
     * @param response informações da resposta
     * @author Gabriel Gois Andrade
     * */
    private fun raiseException(request: HttpServletRequest, response: HttpServletResponse, message: String) {
        response.apply {
            this.contentType = MediaType.APPLICATION_JSON_VALUE
            this.status = HttpServletResponse.SC_UNAUTHORIZED
        }

        val apiError = ApiErrors(message, request.requestURI)

        val body: ByteArray = ObjectMapper().writeValueAsBytes(apiError)

        response.outputStream.write(body)
    }

}