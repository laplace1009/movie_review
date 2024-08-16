package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository: JpaRepository<Movie, Long> {
}