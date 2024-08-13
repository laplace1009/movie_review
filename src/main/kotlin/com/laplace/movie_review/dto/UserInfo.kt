package com.laplace.movie_review.dto

import util.AuthProviderName

data class UserInfo(
    val username: String?,
    val email: String,
    val password: String?,
    val providerName: AuthProviderName,
    val providerId: String?
)
