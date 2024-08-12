package com.laplace.movie_review.service

import com.laplace.movie_review.dto.UserInfo
import com.laplace.movie_review.entity.User
import com.laplace.movie_review.repository.UserRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val response: HttpServletResponse,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createUser(userInfo: UserInfo): User {
        return userRepository.findByEmail(userInfo.email) ?: userInfo.run {
            User(
                username ?: generateRandomUserName(),
                email,
                password?.let { passwordEncoder.encode(it) },
                LocalDateTime.now()
            ).also { userRepository.save(it) }
        }
    }

    fun login(userInfo: UserInfo): String {
        try {
            val user = createUser(userInfo)
            // token 받기 및 처리
            val (accessToken, refreshToken) = jwtTokenProvider.getAccessToken(user.username, listOf("admin"))
            // token 쿠키에 셋업 하기
            setTokenCookie("access_token", accessToken, 3600)
            setTokenCookie("refresh_token", refreshToken, 24 * 60 * 60)
        } catch (ex: BadCredentialsException) {
            throw ex
        }

        return ""
    }

    private fun generateRandomUserName(): String {
        val randomNumber = UUID.randomUUID().toString().replace("_", "").take(8)
        return "user$randomNumber"
    }

    private fun setTokenCookie(key: String, token: String, age: Int) {
        val accessCookie = Cookie(key, token)
        accessCookie.path = "/"
        accessCookie.isHttpOnly = true
        accessCookie.maxAge = age
        response.addCookie(accessCookie)
    }
}