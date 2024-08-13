package com.laplace.movie_review.service

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

    fun getAccessToken(username: String, roles: List<String>, tokenUnit: TokenUnit): Pair<String, Date> {
        return when (tokenUnit.token) {
            TokenUnit.ACCESS_TOKEN.token -> createToken(username, roles, Duration.ofHours(1))
            TokenUnit.REFRESH_TOKEN.token -> createToken(username, roles, Duration.ofDays(180))
            // 도달 할 수 없음
            else -> throw IllegalArgumentException("Invalid token type")
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

//    private fun calculateTime(validityExtension: String): Long {
//        val extensionSecond = validityExtension.let {
//            val timeUnit = TimeUnit.fromString(it)
//            val amount = it.replace(timeUnit?.unit ?: "", "").trim().toLongOrNull() ?: 0L
//
//            when (timeUnit) {
//                TimeUnit.SECONDS -> amount * TimeMillSecond.SECOND.millis
//                TimeUnit.MINUTES -> amount * TimeMillSecond.MINUTE.millis
//                TimeUnit.HOURS -> amount * TimeMillSecond.HOUR.millis
//                TimeUnit.DAYS -> amount * TimeMillSecond.DAY.millis
//                else -> 0
//            }
//        }
//
//        return extensionSecond
//    }
}