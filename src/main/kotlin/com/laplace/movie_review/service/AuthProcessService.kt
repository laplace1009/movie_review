package com.laplace.movie_review.service

import com.laplace.movie_review.entity.User
import com.laplace.movie_review.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthProcessService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun createUser(username: String, email: String, password: String?): User {
        val user = User(
            username,
            email,
            password?.let { passwordEncoder.encode(it) },
            LocalDateTime.now(),
        )

        return userRepository.save(user)
    }

//    fun processOAuth2User(oAuth2User: OAuth2User): User {
//        val email = oAuth2User.attributes["email"] as String
//        val user = userRepository.findByEmail(email)
//
//        return user ?: createUser(generateRandomUserName(), email, null)
//    }
//
//    fun processNativeUser(loginRequest: LoginRequest): User {
//        val user = userRepository.findByEmail(loginRequest.email) ?: throw RuntimeException("User not found")
//        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
//            throw BadCredentialsException("Wrong password")
//        }
//
//        return user
//    }

//    private fun generateRandomUserName(): String {
//        val randomNumber = UUID.randomUUID().toString().replace("_", "").take(8)
//        return "user$randomNumber"
//    }
}