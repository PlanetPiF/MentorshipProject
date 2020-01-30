package com.softserveinc.demo.repository;

import com.softserveinc.demo.model.Cinema;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends CrudRepository<Cinema, Long> {

    Cinema getByName(String name);

}
