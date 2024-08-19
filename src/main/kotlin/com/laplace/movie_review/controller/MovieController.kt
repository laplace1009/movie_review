package com.laplace.movie_review.controller

import com.laplace.movie_review.dto.movie.*
import com.laplace.movie_review.service.MovieService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        @RequestParam director: String, @RequestParam genres: String): ResponseEntity<MovieCreateDTO> {
        val movieDTO = MovieCreateDTO(
            title, description, director, "2024-01-01", genres.split(", ").toSet()
        )
        movieService.createMovieWithGenres(movieDTO)

        return ResponseEntity.status(HttpStatus.CREATED).body(movieDTO)
    }
}