package com.softserveinc.demo.repository;

import com.softserveinc.demo.model.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    List<Cinema> findAllByNameAndHallsAndIsOpen(String name, Integer halls, Boolean isOpen, Pageable pageable);
    List<Cinema> findAllByNameOrHallsOrIsOpen(String name, Integer halls, Boolean isOpen, Pageable pageable);

}
