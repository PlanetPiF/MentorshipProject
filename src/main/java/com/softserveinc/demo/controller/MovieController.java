package com.softserveinc.demo.controller;

import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.repository.MovieRepository;
import com.softserveinc.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class MovieController {
    
    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieService.findAllMovies();
    }

    @GetMapping("/movies/{id}")
    Movie getMovieById(@PathVariable
                       @Min(value = 1, message = "id must be greater than or equal to 1")
                       @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
        return movieService.getById(id);
    }

    // POST/PUT/DELETE not needed currently, implemented for CinemaController

}
