package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.movie.*
import com.laplace.movie_review.service.MovieService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDateTime

@RestController
@RequestMapping("/movie")
class MovieController(private val movieService: MovieService) {
    @GetMapping
    fun movieMainTemplate(): ModelAndView {
        val view = ModelAndView("/movie/main")
        view.addObject("movie", "1234")
        return view
    }

    @GetMapping("/create")
    fun createMovieTemplate(): ModelAndView {
        val view = ModelAndView("/movie/create")

        return view
    }

    @PostMapping("/create")
    fun createMovie(
        @RequestParam title: String, @RequestParam description: String,
        @RequestParam director: String, @RequestParam genres: String): String {
        val movieDTO = MovieCreateDTO(
            title, description, director, LocalDateTime.now(), genres.split(", ").toSet()
        )
        val movie = movieService.createMovieWithGenres(movieDTO)

        return "create ${movie.title} review successful"
    }
}