package com.laplace.movie_review.repository

import com.laplace.movie_review.entity.Genre
import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository: JpaRepository<Genre, Long> {
    fun findByGenreNameIn(genreNames: Set<String>): List<Genre>?
    fun findByGenreName(genreName: String): Genre?
}