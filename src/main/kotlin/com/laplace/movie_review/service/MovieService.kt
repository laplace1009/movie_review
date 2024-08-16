package com.laplace.movie_review.service

import com.laplace.movie_review.dto.genre.GenreModifyDTO
import com.laplace.movie_review.dto.genre.toEntity
import com.laplace.movie_review.dto.movie.MovieCreateDTO
import com.laplace.movie_review.dto.movie.toEntity
import com.laplace.movie_review.entity.Genre
import com.laplace.movie_review.entity.Movie
import com.laplace.movie_review.repository.GenreRepository
import com.laplace.movie_review.repository.MovieRepository
import com.laplace.movie_review.util.ActionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) {
    @Transactional
    fun createMovieWithGenres(movieCreateDTO: MovieCreateDTO): Movie {
        val movie = movieCreateDTO.toEntity()
        val existGenre = genreRepository.findByGenreNameIn(movieCreateDTO.genres)?.toSet() ?: setOf()

        existGenre.forEach{ genre ->
            movie.addGenre(genre)
        }

        movieCreateDTO.genres.filter { gName ->
            existGenre.none { a -> a.genreName == gName }
        }.forEach { name ->
            val genre = Genre(name)
            genreRepository.save(genre)
            movie.addGenre(genre)
        }
        return movieRepository.save(movie)
    }

    @Transactional
    fun modifyGenreToMovie(genreModifyDTO: GenreModifyDTO, actionType: ActionType): Movie {
        val storedMovie = movieRepository.findById(genreModifyDTO.id).orElseThrow {
            throw IllegalStateException("Movie not found with id: ${genreModifyDTO.id}")
        }
        val genre = genreRepository.findByGenreName(genreModifyDTO.genreName)
            ?: genreRepository.save(genreModifyDTO.toEntity())

        when (actionType) {
            ActionType.ADD -> {
                storedMovie.addGenre(genre)
            }
            ActionType.DELETE -> {
                storedMovie.removeGenre(genre)
            }
        }

        return movieRepository.save(storedMovie)
    }
}