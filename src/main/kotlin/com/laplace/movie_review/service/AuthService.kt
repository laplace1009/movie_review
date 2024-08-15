package com.laplace.movie_review.service

import com.laplace.movie_review.dto.AccountInfo
import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.repository.AccountProviderRepository
import com.laplace.movie_review.repository.AccountRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val accountRepository: AccountRepository,
    private val accountProviderRepository: AccountProviderRepository,
    @Lazy private val passwordEncoder: PasswordEncoder,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = accountRepository.findByEmail(email) ?: throw BadCredentialsException("User not found")

        return User.builder()
            .username(user.email)
            .password(user.password)
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
}