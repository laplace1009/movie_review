package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.User
import com.laplace.movie_review.entity.UserProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProviderRepository: JpaRepository<UserProvider, Long> {
    fun findByUser(user: User): UserProvider?
}