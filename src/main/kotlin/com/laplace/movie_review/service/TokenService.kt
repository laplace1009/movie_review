package com.laplace.movie_review.service

import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.provider.JwtTokenProvider
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.RefreshTokenRepository
import io.jsonwebtoken.JwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import com.laplace.movie_review.util.TokenUnit
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.util.*

@Service
class TokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val accountRepository: AccountRepository,
) {
    @Transactional
    fun generateToken(username: String, roles: List<String>, tokenUnit: TokenUnit): Pair<String, Date> {
        return jwtTokenProvider.generateToken(username, roles, tokenUnit)
    }

    fun validateToken(token: String): Boolean {
        return try {
            jwtTokenProvider.validateToken(token)
            true
        } catch (ex: JwtException) {
            throw ex
        }
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = jwtTokenProvider.getUserDetailsFromToken(token)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    @Transactional(readOnly = true)
    fun getRefreshTokenByEmail(email: String): RefreshToken? {
        return refreshTokenRepository.findByEmail(email)
    }

    fun getUsernameFromToken(token: String): String {
        return jwtTokenProvider.getUsernameFromToken(token)
    }

    fun getRolesFromToken(token: String): List<String> {
        return jwtTokenProvider.getRolesFromToken(token)
    }

    @Transactional
    fun saveToken(email: String, token: String, expiresAt: Date): RefreshToken {
        val account = accountRepository.findByEmail(email)
        return refreshTokenRepository.save(
            RefreshToken(account, token, expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        )
    }
}