package com.laplace.movie_review.entity;

import com.laplace.movie_review.dto.genre.GenreModifyDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "genre_name")
    private String genreName;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();

    public Genre() {
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public Long getId() {
        return id;
    }

    public String getGenreName() {
        return genreName;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public GenreModifyDTO toDTO() {
        return new GenreModifyDTO(this.id, this.genreName);
    }
}
