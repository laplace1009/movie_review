package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.RefreshToken
import com.laplace.movie_review.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByUser(user: User): RefreshToken?
    fun findByUserId(userId: Long): RefreshToken?
}