package com.laplace.movie_review.service

import com.laplace.movie_review.dto.accountProvider.AccountProviderCreateDTO
import com.laplace.movie_review.dto.accountProvider.toEntity
import com.laplace.movie_review.entity.AccountProvider
import com.laplace.movie_review.repository.AccountProviderRepository
import com.laplace.movie_review.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountProviderService(
    private val accountProviderRepository: AccountProviderRepository,
    private val accountRepository: AccountRepository
) {
    @Transactional
    fun createLocalProvider(providerCreateDTO: AccountProviderCreateDTO): AccountProvider {
        val account = accountRepository.findById(providerCreateDTO.accountId).orElseThrow {
            throw IllegalStateException("Invalid Account ID: ${providerCreateDTO.accountId}")
        }
        return accountProviderRepository.save(providerCreateDTO.toEntity(account))
    }
}