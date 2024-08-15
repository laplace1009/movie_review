package com.laplace.movie_review.provider

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
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

    fun getUserDetailsFromToken(token: String): UserDetails {
        val claims = getClaimsFromToken(token)
        val username = claims.payload.subject
        val roles = claims.payload["roles"] as List<String>
        val authorities = roles.map { SimpleGrantedAuthority(it) }

        return User(username, "", authorities)
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun getRolesFromToken(token: String): List<String> {
        val claims = getClaimsFromToken(token)
        return claims.payload["roles"] as List<String>
    }

    fun getClaimsFromToken(token: String): Jws<Claims>  {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: ExpiredJwtException) {
            // 토큰 만료
            throw e
        } catch (e: UnsupportedJwtException) {
            // 지원 불가 JWT 형식
            throw e
        } catch (e: SignatureException) {
            // 서명이 잘못된 경우
            throw e
        } catch (e: IllegalArgumentException) {
            // 빈 토큰 or 부적절한 경우
            throw e
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