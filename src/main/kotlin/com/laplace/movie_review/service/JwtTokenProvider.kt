package com.laplace.movie_review.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import util.TimeMillSecond
import util.TimeUnit
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    lateinit var secretKeyString: String

    lateinit var secretKey: SecretKey

    @PostConstruct
    fun init() {
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyString))
    }

    fun getAccessToken(username: String, roles: List<String>): Pair<String, String> {
        val accessToken = createToken(username, roles, "1hour")
        val refreshToken = createToken(username, roles, "180day")

        return Pair(accessToken, refreshToken)
    }

    private fun createToken(username: String, roles: List<String>, validityExtension: String): String {
        val now = Date()
        val validity = Date(now.time + calculateTime(validityExtension))
        return Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .expiration(validity)
            .signWith(secretKey)
            .compact()
    }

    private fun calculateTime(validityExtension: String): Long {
        val extensionSecond = validityExtension.let {
            val timeUnit = TimeUnit.fromString(it)
            val amount = it.replace(timeUnit?.unit ?: "", "").trim().toLongOrNull() ?: 0L

            when (timeUnit) {
                TimeUnit.SECONDS -> amount * TimeMillSecond.SECOND.millis
                TimeUnit.MINUTES -> amount * TimeMillSecond.MINUTE.millis
                TimeUnit.HOURS -> amount * TimeMillSecond.HOUR.millis
                TimeUnit.DAYS -> amount * TimeMillSecond.DAY.millis
                else -> 0
            }
        }

        return extensionSecond
    }
}