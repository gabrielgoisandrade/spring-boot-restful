package com.gga.restful.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS512
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenProvider {

    @Value("\${security.jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${security.jwt.expire-lenght}")
    private lateinit var validityMilliseconds: String

    private val currentDate: Date = Date()

    private val encodedSecretKey: String by lazy { Base64.getEncoder().encodeToString(this.secretKey.toByteArray()) }

    private val expirationDate: Date by lazy { this.validityMilliseconds.run { Date(currentDate.time + this.toLong()) } }

    private fun getClaims(token: String): Claims = Jwts.parser()
        .setSigningKey(this.encodedSecretKey)
        .parseClaimsJws(token)
        .body

    fun createToken(username: String): String = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(this.currentDate) // data de emissão do token
        .setExpiration(this.expirationDate)
        .signWith(HS512, this.encodedSecretKey)
        .compact()

    fun getUsername(token: String): String = this.getClaims(token).subject

}