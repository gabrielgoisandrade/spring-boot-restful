package com.gga.restful.config

import com.gga.restful.security.JwtTokenFilter
import com.gga.restful.security.JwtTokenProvider
import com.gga.restful.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Bean
    fun bCrypt(): BCryptPasswordEncoder = BCryptPasswordEncoder() //para encriptar a senha

    @Bean
    fun requestFilter(): OncePerRequestFilter = JwtTokenFilter(this.tokenProvider, this.authenticationService)

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(this.authenticationService)?.passwordEncoder(this.bCrypt())
    }

    override fun configure(http: HttpSecurity) {
        http.httpBasic().disable().csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/user", "/book", "/auth").permitAll()
            .antMatchers(HttpMethod.GET, "/user", "/book").authenticated()
            .antMatchers(HttpMethod.PUT, "/user", "/book").authenticated()
            .antMatchers(HttpMethod.DELETE, "/user", "/book").authenticated()
            .and().addFilterBefore(this.requestFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/h2-console/**"
            )
    }
}