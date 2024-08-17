package com.laplace.movie_review.dto.movie

import com.laplace.movie_review.entity.Movie
import java.time.LocalDateTime

data class MovieCreateDTO(
    val title: String,
    val description: String,
    val director: String,
    val releaseDate: LocalDateTime,
    val genres: Set<String>,
)

fun MovieCreateDTO.toEntity(): Movie {
    return Movie(title, description, director, releaseDate)
}

