package com.laplace.movie_review.dto.accountProvider

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider

data class AccountProviderCreateDTO(
    val accountId: Long,
    val providerName: String,
    val providerId: String
)

fun AccountProviderCreateDTO.toEntity(account: Account): AccountProvider {
    return AccountProvider(account, providerName, providerId)
}
