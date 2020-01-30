package com.softserveinc.demo.service;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;

import java.util.List;
import java.util.Optional;

public interface CinemaService {

    Cinema getById(Long id);
    Optional<Cinema> findById(Long id);
    Cinema getByName(String name);
    List<Cinema> getAllCinemas();
    List<Cinema> getOpenCinemas();
    List<Cinema> getClosedCinemas();
    List<Cinema> getByMovie(Movie movie);

    Cinema save(Cinema cinema);
     void deleteById(Long id);

}
