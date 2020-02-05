package com.softserveinc.demo.service;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Cinema> getAllCinemas() {
        return (List<Cinema>) cinemaRepository.findAll();
    }

    @Override
    public List<Cinema> findAllByNameOrHallsOrIsOpenOrMovies_Id(String name, Integer halls, Boolean isOpen, Long movieId, Pageable pageable) {
        return cinemaRepository.findAllByNameOrHallsOrIsOpenOrMovies_Id(name, halls, isOpen, movieId, pageable);
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
