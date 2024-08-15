package com.laplace.movie_review.service

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.repository.AccountProviderRepository
import com.laplace.movie_review.repository.AccountRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import com.laplace.movie_review.util.AuthProviderName
import java.util.*

@Service
class CustomOAuth2UserService(
    private val accountRepository: AccountRepository,
    private val accountProviderRepository: AccountProviderRepository
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val email = oAuth2User.attributes["email"] as? String
            ?: throw OAuth2AuthenticationException("Email not found from OAuth2 provider")
        val providerId = oAuth2User.attributes["sub"] as? String
            ?: throw OAuth2AuthenticationException("ProviderId not found from OAuth2 provider")

        val account = accountRepository.findByEmail(email)
            ?: accountRepository.save(Account(generateRandomUserName(), email, null))

        accountProviderRepository.save(AccountProvider(account, AuthProviderName.GOOGLE.providerName, providerId))

        return oAuth2User
    }

    // userId 랜덤 생성
    private fun generateRandomUserName(): String {
        val randomNumber = UUID.randomUUID().toString().replace("_", "").take(8)
        return "user$randomNumber"
    }
}