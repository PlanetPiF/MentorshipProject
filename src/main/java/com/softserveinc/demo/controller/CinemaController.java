package com.softserveinc.demo.controller;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
public class CinemaController {

    private CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping(value = "/cinemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Cinema> getCinemas(@RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "open", required = false) Boolean open,
                                   @RequestParam(name = "movieId", required = false) Long movieId,
                                   @RequestParam(name = "halls", required = false) Integer halls,
                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                   @RequestParam(name = "size", required = false, defaultValue = "100") Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        // Return all movies if there are no filters
        if (name == null && open == null && movieId == null && halls == null) {
            return cinemaService.findAll(pageable);
        }

        return cinemaService.findCinemasBy(name,halls,open,movieId,pageable);
    }

    @GetMapping(value = "/cinemas/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Cinema getCinemaById(@PathVariable
                         @Min(value = 1, message = "id must be greater than or equal to 1")
                         @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
        return cinemaService.getById(id);
    }

    @PostMapping(value = "/cinemas",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Cinema createOrSaveCinema(@RequestBody Cinema newCinema) {
        return cinemaService.save(newCinema);
    }

    @DeleteMapping("/cinemas/{id}")
    void deleteCinema(@PathVariable Long id) {
        cinemaService.deleteById(id);
    }

    @PutMapping(value = "/cinemas/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Cinema updateCinema(@RequestBody Cinema newCinema, @PathVariable Long id) {
        return cinemaService.findById(id).map(cinema -> {
            cinema.setName(newCinema.getName());
            cinema.setOpen(newCinema.isOpen());
            cinema.setMovies(newCinema.getMovies());
            cinema.setAddress(newCinema.getAddress());
            cinema.setHalls(newCinema.getHalls());
            return cinemaService.save(cinema);
        }).orElseGet(() -> {
            newCinema.setId(id);
            return cinemaService.save(newCinema);
        });
    }

}
