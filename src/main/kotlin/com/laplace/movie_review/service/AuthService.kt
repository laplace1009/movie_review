package com.laplace.movie_review.service

import com.laplace.movie_review.dto.UserInfo
import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.entity.User
import com.laplace.movie_review.entity.UserProvider
import com.laplace.movie_review.repository.RefreshTokenRepository
import com.laplace.movie_review.repository.UserProviderRepository
import com.laplace.movie_review.repository.UserRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import util.AuthProviderName
import util.TokenUnit
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val response: HttpServletResponse,
    private val userRepository: UserRepository,
    private val userProviderRepository: UserProviderRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun createUser(userInfo: UserInfo): User {
        // 만약 이메일이 없다면 계정을 만듬
        return userRepository.findByEmail(userInfo.email) ?: userInfo.run {
            val user = User(
                username ?: generateRandomUserName(),
                email,
                password?.let { passwordEncoder.encode(it) },
                LocalDateTime.now()
            )

            val savedUser = userRepository.save(user)
            userProviderRepository.save(
                UserProvider(savedUser, userInfo.providerName.providerName, userInfo.providerId)
            )
            savedUser
        }
    }

    fun login(userInfo: UserInfo): String {
        // 로컬 로그인이고 이메일 입력 안했을 때
        if (userInfo.providerName == AuthProviderName.LOCAL && userInfo.email.trim().isBlank()) {
            throw BadCredentialsException("Email cannot be empty")
        }
        val user = createUser(userInfo)

        // 로컬 로그인이고 비밀번호가 다를 때
        if (userInfo.providerName == AuthProviderName.LOCAL
            && !passwordEncoder.matches(userInfo.password, user.password)) {
            throw BadCredentialsException("Password does not match")
        }

        // token 받기 및 처리
        val refreshToken = refreshTokenRepository.findByUser(user)?.let { token ->
            if (token.expiresAt.isBefore(LocalDateTime.now())) {
                val (newRefreshToken, expiresAt) = jwtTokenProvider.getAccessToken(user.username, listOf("admin"), TokenUnit.REFRESH_TOKEN)
                refreshTokenRepository.save(RefreshToken(user, newRefreshToken, LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault())))
                newRefreshToken
            } else {
                token.token
            }
        } ?: run {
            val (newRefreshToken, expiresAt) = jwtTokenProvider.getAccessToken(user.username, listOf("admin"), TokenUnit.REFRESH_TOKEN)
            refreshTokenRepository.save(RefreshToken(user, newRefreshToken, LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault())))
            newRefreshToken
        }

        val (accessToken, _) = jwtTokenProvider.getAccessToken(user.username, listOf("admin"), TokenUnit.ACCESS_TOKEN)
        val tokenList = listOf(TokenUnit.REFRESH_TOKEN, TokenUnit.ACCESS_TOKEN)
            .zip(listOf(refreshToken, accessToken))

        // token 쿠키에 셋업 하기
        setTokenCookie(tokenList)

        return "Login Successful"
    }

    // userId 랜덤 생성
    private fun generateRandomUserName(): String {
        val randomNumber = UUID.randomUUID().toString().replace("_", "").take(8)
        return "user$randomNumber"
    }

    private fun setTokenCookie(tokenList: List<Pair<TokenUnit, String>>) {
        tokenList.map { ele ->
            Cookie(ele.first.token, ele.second)
        }.map { cookie ->
            cookie.apply {
                path = "/"
                isHttpOnly = true
                maxAge = when (name) {
                    TokenUnit.REFRESH_TOKEN.token -> 24 * 3600
                    TokenUnit.ACCESS_TOKEN.token -> 3600
                    else -> throw IllegalArgumentException("Invalid token type")
                }
            }
        }.forEach { cookie -> response.addCookie(cookie) }
    }
}