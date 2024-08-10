package com.laplace.movie_review.service

import com.laplace.movie_review.entity.User
import com.laplace.movie_review.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(private val userRepository: UserRepository) {
    fun createUser(username: String, email: String, password: String): User {
        val passwordEncoder = BCryptPasswordEncoder()
        val encryptedPassword = passwordEncoder.encode(password)
        val user = User(
            username,
            email,
            encryptedPassword,
            LocalDateTime.now()
        )
        return userRepository.save(user)
    }

    fun loginUser(email: String, password: String): Boolean {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            return correctPassword(password, user.password)
        }
        return false
    }

    private fun correctPassword(inputPassword: String, dbPassword: String): Boolean {
        println("$inputPassword $dbPassword")
        return inputPassword == dbPassword
    }

}