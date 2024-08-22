package com.laplace.movie_review.dto.token

import com.laplace.movie_review.entity.Account
import com.laplace.movie_review.entity.RefreshToken
import java.time.ZoneId
import java.util.*

data class RefreshTokenSaveDTO(val email: String, val token: String, val expiresAt: Date)

fun RefreshTokenSaveDTO.toEntity(account: Account): RefreshToken {
    return RefreshToken(account, token, expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
}
