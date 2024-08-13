package com.laplace.movie_review.provider

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import util.TokenUnit
import java.util.*
import javax.crypto.SecretKey
import java.time.Duration


@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    lateinit var secretKeyString: String

    lateinit var secretKey: SecretKey

    @PostConstruct
    fun init() {
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyString))
    }

    fun generateToken(username: String, roles: List<String>, tokenUnit: TokenUnit): Pair<String, Date> {
        return when (tokenUnit.token) {
            TokenUnit.ACCESS_TOKEN.token -> createToken(username, roles, Duration.ofHours(1))
            TokenUnit.REFRESH_TOKEN.token -> createToken(username, roles, Duration.ofDays(180))
            else -> throw IllegalArgumentException("Invalid token type")
        }
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(token: String): Boolean {
        try {
            val payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            println(payload.toString())
            return true
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token")
        }
    }

    private fun createToken(username: String, roles: List<String>, validityExtension: Duration): Pair<String, Date> {
        val now = Date()
        val validity = Date(now.time + validityExtension.toMillis())
        val token = Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .expiration(validity)
            .signWith(secretKey)
            .compact()

        return Pair(token, validity)
    }
}