package com.laplace.movie_review.dto.account

import com.laplace.movie_review.entity.Account
import org.springframework.security.crypto.password.PasswordEncoder

data class AccountLocalCreateDTO(
    val username: String,
    val email: String,
    val password: String,
)

fun AccountLocalCreateDTO.toEntity(passwordEncoder: PasswordEncoder): Account {
    return Account(username, email, passwordEncoder.encode(password))
}
