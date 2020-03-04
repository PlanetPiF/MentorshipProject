package com.softserveinc.demo.controller;

import com.softserveinc.demo.misc.RestResponseEntityExceptionHandler;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    public void testGetById() throws Exception {
        Movie movie = new Movie("Batman", Boolean.TRUE, 200);
        when(movieService.getById(1L)).thenReturn(movie);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title" , is("Batman")))
                .andReturn();
    }

    @Test
    public void testFindAll() throws Exception {
        List<Movie> moviesList = new ArrayList<>();
        moviesList.add(new Movie("Superman", Boolean.TRUE, 200));
        moviesList.add(new Movie("Matrix4", Boolean.FALSE, 300));

        Mockito.when(movieService.findAllMovies()).thenReturn(moviesList);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].title", is("Superman")))
                .andExpect(jsonPath("$.[0].released", is(true)))
                .andExpect(jsonPath("$.[1].title", is("Matrix4")))
                .andReturn();
    }

}
