package com.softserveinc.demo.service;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CinemaServiceImpl implements CinemaService{

    @Autowired
    private CinemaRepository cinemaRepository;


    @Override
    public Cinema getById(Long id) {
        return cinemaRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Cinema> findById(Long id) {
        return cinemaRepository.findById(id);
    }

    @Override
    public Cinema getByName(String name) {
        return cinemaRepository.getByName(name);
    }

    @Override
    public List<Cinema> getAllCinemas() {
        return (List<Cinema>) cinemaRepository.findAll();
    }

    @Override
    public List<Cinema> getOpenCinemas() {
        List<Cinema> results = (List<Cinema>) cinemaRepository.findAll();
        return results.stream().filter(Cinema::isOpen).collect(Collectors.toList());
    }

    @Override
    public List<Cinema> getClosedCinemas() {
        List<Cinema> results = (List<Cinema>) cinemaRepository.findAll();
        return results.stream().filter(cinema -> !cinema.isOpen()).collect(Collectors.toList());
    }

    @Override
    public List<Cinema> getByMovie(Movie movie) {
        List<Cinema> results = (List<Cinema>) cinemaRepository.findAll();
        return results.stream().filter(cinema -> cinema.getMovies().contains(movie)).collect(Collectors.toList());
    }

    @Override
    public Cinema save(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    @Override
    public void deleteById(Long id) {
       cinemaRepository.deleteById(id);
    }
}
