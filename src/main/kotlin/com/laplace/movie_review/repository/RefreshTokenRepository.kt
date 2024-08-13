package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByUser(account: Account): RefreshToken?
    fun findByUserId(userId: Long): RefreshToken?
}