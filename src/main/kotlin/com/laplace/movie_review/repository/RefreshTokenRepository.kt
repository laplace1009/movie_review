package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByAccount(account: Account): RefreshToken?
    fun findByAccountId(accountId: Long): RefreshToken?

    @Query("SELECT rt FROM RefreshToken rt INNER JOIN rt.account a WHERE a.email = :email")
    fun findByEmail(email: String): RefreshToken?
}