package com.softserveinc.demo.repository;

import com.softserveinc.demo.model.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    Page<Cinema> findAllByNameOrHallsOrIsOpenOrMovies_Id(String name, Integer halls, Boolean isOpen, Long id, Pageable pageable);
    Page<Cinema> findAllByNameOrHallsOrIsOpen(String name, Integer halls, Boolean isOpen, Pageable pageable);
    Page<Cinema> findAll(Pageable pageable);
}
