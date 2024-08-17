package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, Long> {
}