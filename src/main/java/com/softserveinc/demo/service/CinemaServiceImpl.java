package com.softserveinc.demo.service;

import com.softserveinc.demo.exception.EntityNotFoundException;
import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CinemaServiceImpl implements CinemaService{

    private CinemaRepository cinemaRepository;

    public CinemaServiceImpl () {}

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public Cinema getById(Long id) {
        return cinemaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema not found"));
    }

    @Override
    public Optional<Cinema> findById(Long id) {
        return cinemaRepository.findById(id);
    }

    @Override
    public Page<Cinema> findAll(Pageable pageable) {
        return cinemaRepository.findAll(pageable);
    }


    @Override
    public Page<Cinema> findCinemasBy(String name, Integer halls, Boolean isOpen, Long movieId, Pageable pageable) {
        if(movieId == null) {
            return cinemaRepository.findAllByNameOrHallsOrIsOpen(name, halls, isOpen, pageable);
        }

        return cinemaRepository.findAllByNameOrHallsOrIsOpenOrMovies_Id(name, halls, isOpen, movieId, pageable);
    }

    @Override
    public Cinema save(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    @Override
    public void deleteById(Long id) {
        try {
            cinemaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Cinema not found with id: " + id);
        }
    }
}
