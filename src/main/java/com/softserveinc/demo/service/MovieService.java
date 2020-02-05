package com.softserveinc.demo.service;

import com.softserveinc.demo.model.Movie;

import java.util.List;

public interface MovieService {

    Movie getById(Long id);
    List<Movie> findAllMovies();
}
