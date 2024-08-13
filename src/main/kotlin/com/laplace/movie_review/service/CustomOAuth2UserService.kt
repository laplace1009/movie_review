package com.laplace.movie_review.service

import com.laplace.movie_review.dto.UserInfo
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import util.AuthProviderName

@Service
class CustomOAuth2UserService(
    private val authService: AuthService
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val email = oAuth2User.attributes["email"] as? String
            ?: throw OAuth2AuthenticationException("Email not found from OAuth2 provider")
        val providerId = oAuth2User.attributes["sub"] as? String
            ?: throw OAuth2AuthenticationException("ProviderId not found from OAuth2 provider")

        authService.login(UserInfo(username = null, email, password = null, AuthProviderName.GOOGLE, providerId))
        return oAuth2User
    }
}