package com.laplace.movie_review.service

import com.laplace.movie_review.dto.token.RefreshTokenSaveDTO
import com.laplace.movie_review.dto.token.toEntity
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
import java.util.*
import javax.security.auth.login.AccountNotFoundException

@Service
class TokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val accountRepository: AccountRepository,
) {
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
    fun saveToken(dto: RefreshTokenSaveDTO): RefreshTokenSaveDTO {
        val account = accountRepository.findByEmail(dto.email) ?: throw AccountNotFoundException("${dto.email} not found")
        return refreshTokenRepository.save(dto.toEntity(account)).toDto()
    }
}