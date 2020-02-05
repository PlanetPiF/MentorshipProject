package com.softserveinc.demo.controller;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.service.CinemaService;
import com.softserveinc.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;

@RestController
public class CinemaController {

    private CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping(value = "/cinemas")
    public List<Cinema> getCinemas(@RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "isOpen", required = false) Boolean isOpen,
                                   @RequestParam(name = "movieId", required = false) Long movieId,
                                   @RequestParam(name = "halls", required = false) Integer halls,
                                   @RequestParam(name = "page", required = false) Integer page,
                                   @RequestParam(name = "size", required = false) Integer size) {

        // Create default pagination
        if(page == null) {
            page = 0; // first page is actually 0
        }
        if(size == null) {
            size = 10; //default page size = 10
        }
        Pageable pageable = PageRequest.of(page, size);

        // Return all movies if there are no filters
        if (name == null && isOpen == null && movieId == null && halls == null) {
            return cinemaService.getAllCinemas();
        }

        return cinemaService.findAllByNameOrHallsOrIsOpenOrMovies_Id(name,halls,isOpen,movieId,pageable);
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
