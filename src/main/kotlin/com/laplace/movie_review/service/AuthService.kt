package com.laplace.movie_review.service

import com.laplace.movie_review.dto.AccountInfo
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.repository.AccountProviderRepository
import com.laplace.movie_review.repository.AccountRepository
import com.laplace.movie_review.repository.RefreshTokenRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import util.TokenUnit
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val accountRepository: AccountRepository,
    private val accountProviderRepository: AccountProviderRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = accountRepository.findByEmail(email) ?: throw BadCredentialsException("User not found")

        return User.builder()
            .username(user.email)
            .password(passwordEncoder.encode(user.password))
            .roles("ADMIN")
            .build()
    }

    fun createLocalUser(accountInfo: AccountInfo) {
        // 만약 이메일이 없다면 계정을 만듬
        accountRepository.findByEmail(accountInfo.email) ?: accountInfo.run {
            val account = Account(
                username,
                email,
                passwordEncoder.encode(password)
            )

            val savedUser = accountRepository.save(account)
            accountProviderRepository.save(
                AccountProvider(
                    savedUser,
                    accountInfo.providerName.providerName,
                    accountInfo.providerId
                )
            )
        }
    }

    fun saveRefreshToken(refreshToken: String) {
        val refreshToken
    }

//    private fun setTokenCookie(tokenList: List<Pair<TokenUnit, String>>) {
//        tokenList.map { ele ->
//            Cookie(ele.first.token, ele.second)
//        }.map { cookie ->
//            cookie.apply {
//                path = "/"
//                isHttpOnly = true
//                maxAge = when (name) {
//                    TokenUnit.REFRESH_TOKEN.token -> 24 * 3600
//                    TokenUnit.ACCESS_TOKEN.token -> 3600
//                    else -> throw IllegalArgumentException("Invalid token type")
//                }
//            }
//        }.forEach { cookie -> response.addCookie(cookie) }
//    }
}