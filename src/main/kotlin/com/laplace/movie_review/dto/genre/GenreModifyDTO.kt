package com.laplace.movie_review.dto.genre

import com.laplace.movie_review.entity.Genre

data class GenreModifyDTO(val id: Long, val genreName: String)

fun GenreModifyDTO.toEntity(): Genre {
    return Genre(genreName)
}
