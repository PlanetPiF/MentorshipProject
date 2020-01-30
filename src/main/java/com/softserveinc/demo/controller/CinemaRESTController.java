package com.softserveinc.demo.controller;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.service.CinemaService;
import com.softserveinc.demo.service.MovieService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class CinemaRESTController {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private MovieService movieService;

    @GetMapping(value = "/cinemas")
    public List<Cinema> getCinemas(@RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "isOpen", required = false) Boolean isOpen,
                                   @RequestParam(name = "movieId", required = false) Long movieId
    ) {

        if (name == null && isOpen == null && movieId == null) {
            return cinemaService.getAllCinemas();
        } else {
            Set<Cinema> set = new HashSet<>();

            // Search for cinemas playing a specific movie
            if (movieId != null) {
                Movie movie = movieService.getById(movieId);
                set.addAll(cinemaService.getByMovie(movie));
            }

            // Search cinema by name
            if (Strings.isNotBlank(name)) {
                set.add(cinemaService.getByName(name));
            }

            // Search only for open cinemas
            if (isOpen != null) {
                if (isOpen) {
                    set.addAll(cinemaService.getOpenCinemas());
                } else {
                    set.addAll(cinemaService.getClosedCinemas());
                }
            }

            return new ArrayList<>(set);
        }

    }

    @GetMapping("/cinemas/{id}")
    Cinema getCinemaById(@PathVariable
                         @Min(value = 1, message = "id must be greater than or equal to 1")
                         @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
        return cinemaService.getById(id);
    }

    @PostMapping("/cinemas")
    Cinema createOrSaveCinema(@RequestBody Cinema newCinema) {
        return cinemaService.save(newCinema);
    }

    @DeleteMapping("/cinemas/{id}")
    void deleteCinema(@PathVariable Long id) {
        cinemaService.deleteById(id);
    }

    @PutMapping("/employees/{id}")
    Cinema updateCinema(@RequestBody Cinema newCinema, @PathVariable Long id) {
        return cinemaService.findById(id).map(cinema -> {
            cinema.setName(newCinema.getName());
            cinema.setOpen(newCinema.isOpen());
            cinema.setMovies(newCinema.getMovies());
            return cinemaService.save(cinema);
        }).orElseGet(() -> {
            newCinema.setId(id);
            return cinemaService.save(newCinema);
        });
    }

}
