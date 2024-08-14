package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.AccountProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountProviderRepository: JpaRepository<AccountProvider, Long> {
    fun findByAccount(account: Account): AccountProvider?
}