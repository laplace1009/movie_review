package com.laplace.movie_review.service

import com.laplace.movie_review.dto.movie.MovieCreateDTO
import com.laplace.movie_review.dto.movie.toEntity
import com.laplace.movie_review.entity.Genre
import com.laplace.movie_review.entity.Movie
import com.laplace.movie_review.repository.GenreRepository
import com.laplace.movie_review.repository.MovieRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class MovieServiceTest {
    @Mock
    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var genreRepository: GenreRepository

    @InjectMocks
    private lateinit var movieService: MovieService

    private lateinit var movieCreateDTO: MovieCreateDTO
    private lateinit var genreList: List<Genre>
    private lateinit var savedMovie: Movie

    @BeforeEach
    fun setUp() {
        movieCreateDTO = MovieCreateDTO("dune", "dune dune", "test", "2024-01-01", setOf())
        genreList = listOf(Genre("action"), Genre("sf"))
        savedMovie = movieCreateDTO.toEntity()
        genreList.forEach { genre -> savedMovie.addGenre(genre) }
    }

    @Test
    fun `createMovieWithGenres should return movie`() {
        `when`(movieRepository.findMovieByTitle(movieCreateDTO.title)).thenReturn(null)
        `when`(genreRepository.findByGenreNameIn(movieCreateDTO.genres)).thenReturn(emptyList())
        `when`(genreRepository.save(any(Genre::class.java))).thenReturn(Genre("action"))
        `when`(movieRepository.save(any(Movie::class.java))).thenReturn(savedMovie)
        val movie = movieService.createMovieWithGenres(movieCreateDTO)
        assertEquals(movie.title, movieCreateDTO.title)
        assertEquals(movie.genres, genreList.toSet())
    }

    @Test
    fun `createMovieWithGenres should return null`() {
        `when`(movieRepository.findMovieByTitle(movieCreateDTO.title)).thenReturn(savedMovie)
        val exception = assertThrows(IllegalStateException::class.java) {
            movieService.createMovieWithGenres(movieCreateDTO)
        }
        assertEquals(exception.message, "same movie exist: ${movieCreateDTO.title}")
    }
}