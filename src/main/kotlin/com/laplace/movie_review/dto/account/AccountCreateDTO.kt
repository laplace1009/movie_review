package com.laplace.movie_review.dto.account

import com.laplace.movie_review.entity.Account
import org.springframework.security.crypto.password.PasswordEncoder

data class AccountCreateDTO(
    val username: String,
    val email: String,
    val password: String,
)

fun AccountCreateDTO.toEntity(passwordEncoder: PasswordEncoder): Account {
    return Account(username, email, passwordEncoder.encode(password))
}
