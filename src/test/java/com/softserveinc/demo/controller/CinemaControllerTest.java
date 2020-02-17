package com.softserveinc.demo.controller;

import com.softserveinc.demo.exception.EntityNotFoundException;
import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.service.CinemaService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CinemaControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CinemaController cinemaController;

    @Mock
    private CinemaService cinemaService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cinemaController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    public void testGetById() throws Exception {
        Cinema cinema = new Cinema("Arena", Boolean.TRUE, 10, "Address4");
        when(cinemaService.getById(4L)).thenReturn(cinema);

        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name" , Matchers.is("Arena")))
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)))
                .andReturn();
    }

    @Test
    public void testNotFound() throws Exception {
        when(cinemaService.getById(1L)).thenThrow(new EntityNotFoundException("Cinema not found."));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cinemas/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status); //NOT FOUND
    }

    @Test
    public void testFindAll() throws Exception {
        Movie movie1 = new Movie("Lord of the Rings", Boolean.TRUE, 200);
        Cinema cinema1 = new Cinema("Arena", Boolean.TRUE, 10, "Address4");
        Cinema cinema2 = new Cinema("IMAX", Boolean.FALSE, 5, "Address8");
        cinema1.getMovies().add(movie1);

        List<Cinema> cinemasList = new ArrayList<>();
        cinemasList.add(cinema1);
        cinemasList.add(cinema2);

        when(cinemaService.findAllCinemas()).thenReturn(cinemasList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cinemas")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].name", is("Arena")))
                .andExpect(jsonPath("$.[0].movies[0].title", is("Lord of the Rings")))
                .andExpect(jsonPath("$.[1].name", is("IMAX")))
                .andReturn();
    }

}
