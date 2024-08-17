package com.laplace.movie_review.service

import com.laplace.movie_review.dto.accountProvider.AccountProviderCreateDTO
import com.laplace.movie_review.dto.accountProvider.toEntity
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.repository.AccountProviderRepository
import com.laplace.movie_review.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountProviderService(
    private val accountProviderRepository: AccountProviderRepository,
    private val accountRepository: AccountRepository
) {
    fun createLocalProvider(providerCreateDTO: AccountProviderCreateDTO): AccountProvider {
        val account = accountRepository.findById(providerCreateDTO.accountId).orElseThrow {
            throw IllegalStateException("Invalid Account ID: ${providerCreateDTO.accountId}")
        }
        return accountProviderRepository.save(providerCreateDTO.toEntity(account))
    }
}