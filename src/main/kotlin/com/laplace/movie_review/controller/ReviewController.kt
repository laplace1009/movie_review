package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.review.ReviewCreateDTO
import com.laplace.movie_review.service.ReviewService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/review")
class ReviewController(private val reviewService: ReviewService) {
    @GetMapping
    fun reviewMainTemplate(): ModelAndView {
        val view = ModelAndView("review/main")
        view.addObject("review", "Review")
        return view
    }

    @GetMapping("/create")
    fun createTemplate(): ModelAndView {
        val view = ModelAndView("review/create")
        return view
    }

    @PostMapping("/create")
    fun createReview(
        @RequestParam accountId: Long,
        @RequestParam movieId: Long,
        @RequestParam rating: Float,
        @RequestParam comment: String
    ) {
        val reviewCreateDTO = ReviewCreateDTO(accountId, movieId, rating, comment)
        reviewService.createReview(reviewCreateDTO)
    }
}