package com.softserveinc.demo.service;

import com.softserveinc.demo.model.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CinemaService {
    Page<Cinema> findAll(Pageable pageable);
    Page<Cinema> findCinemasBy(String name, Integer halls, Boolean isOpen, Long movieId, Pageable pageable);

    Cinema getById(Long id);
    Optional<Cinema> findById(Long id);
    Cinema save(Cinema cinema);
    void deleteById(Long id);
}
