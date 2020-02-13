package com.softserveinc.demo.service;

import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Movie getById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }
}
