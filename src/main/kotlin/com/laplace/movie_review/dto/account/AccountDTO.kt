package com.laplace.movie_review.dto.account

import com.laplace.movie_review.util.AuthProviderName

data class AccountDTO(
    val username: String?,
    val email: String,
    val password: String?,
    val providerName: AuthProviderName,
    val providerId: String?
)
