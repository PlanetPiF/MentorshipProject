package com.softserveinc.demo.controller;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.service.CinemaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CinemaControllerTest {

    @InjectMocks
    private CinemaController cinemaController;

    @Mock
    private CinemaService cinemaService;

    @Test
    public void testFindAll()
    {
        Movie movie1 = new Movie("Lord of the Rings", Boolean.TRUE, 200);
        Movie movie2 = new Movie("Garfield 5", Boolean.FALSE, 1);
        Cinema cinema1 = new Cinema("Arena", Boolean.TRUE, 10, "Address4");
        Cinema cinema2 = new Cinema("IMAX", Boolean.FALSE, 5, "Address8");
        cinema1.getMovies().add(movie1);
        cinema2.getMovies().add(movie2);

        List<Cinema> cinemasList = new ArrayList<>();
        cinemasList.add(cinema1);
        cinemasList.add(cinema2);

        when(cinemaService.findAllCinemas()).thenReturn(cinemasList);

        List<Cinema> result = cinemaController.getCinemas(null, null, null, null,null,null);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getName(), is(cinema1.getName()));
    }

}
