package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Long> {
}