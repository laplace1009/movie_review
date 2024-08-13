package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByUsername(username: String): Account?
    fun findByEmail(email: String): Account?
}