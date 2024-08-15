package com.laplace.movie_review

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.laplace.movie_review"])
class MovieReviewApplication

fun main(args: Array<String>) {
    runApplication<MovieReviewApplication>(*args)
}
