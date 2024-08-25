package com.laplace.movie_review.service

import com.laplace.movie_review.dto.account.AccountLocalCreateDTO
import com.laplace.movie_review.dto.account.AccountInfoDTO
import com.laplace.movie_review.dto.account.toEntity
import com.laplace.movie_review.repository.AccountRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    @Lazy private val passwordEncoder: PasswordEncoder,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = accountRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")

        return User.builder()
            .username(user.email)
            .password(user.password)
            .roles("ADMIN")
            .build()
    }

    @Transactional
    fun createLocalUser(accountLocalCreateDTO: AccountLocalCreateDTO): Long {
        // 만약 이메일이 없다면 계정을 만듬
        val account = accountRepository.findByEmail(accountLocalCreateDTO.email) ?: accountLocalCreateDTO.toEntity(passwordEncoder)
        return accountRepository.save(account).id
    }

    fun getCurrentAccount(): AccountInfoDTO {
        val principal = SecurityContextHolder.getContext().authentication.principal as? User
            ?: throw IllegalStateException("Current user is not authenticated")
        return AccountInfoDTO(principal.username)
    }
}