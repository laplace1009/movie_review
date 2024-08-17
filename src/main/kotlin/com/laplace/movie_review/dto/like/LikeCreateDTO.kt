package com.laplace.movie_review.dto.like

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.Like
import com.laplace.movie_review.entity.Review

data class LikeCreateDTO(val accountId: Long, val reviewId: Long)

fun LikeCreateDTO.toEntity(account: Account, review: Review): Like {
    return Like(account, review)
}
