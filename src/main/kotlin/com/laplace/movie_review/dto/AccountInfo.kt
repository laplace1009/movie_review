package com.laplace.movie_review.dto

import com.laplace.movie_review.util.AuthProviderName

data class AccountInfo(
    val username: String?,
    val email: String,
    val password: String?,
    val providerName: AuthProviderName,
    val providerId: String?
)
