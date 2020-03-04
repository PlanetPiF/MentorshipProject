package com.softserveinc.demo.controller;

import com.google.gson.Gson;
import com.softserveinc.demo.exception.EntityNotFoundException;
import com.softserveinc.demo.misc.RestResponseEntityExceptionHandler;
import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.service.CinemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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
                .andExpect(jsonPath("$.name" , is("Arena")))
                .andExpect(jsonPath("$.*", hasSize(6)))
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

        PageImpl<Cinema> pagedResponse = new PageImpl<>(cinemasList);
        Mockito.when(cinemaService.findAll(any(PageRequest.class))).thenReturn(pagedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content.[0].name", is("Arena")))
                .andExpect(jsonPath("$.content.[0].movies[0].title", is("Lord of the Rings")))
                .andExpect(jsonPath("$.content.[1].name", is("IMAX")))
                .andReturn();
    }

    @Test
    public void testFindWithFilters() throws Exception {
        Movie movie1 = new Movie("Lord of the Rings", Boolean.TRUE, 200);
        movie1.setId(1L);
        Cinema cinema1 = new Cinema("Arena", Boolean.TRUE, 10, "Address4");
        cinema1.getMovies().add(movie1);

        List<Cinema> cinemasList = new ArrayList<>();
        cinemasList.add(cinema1);

        PageImpl<Cinema> pagedResponse = new PageImpl<>(cinemasList);
        Mockito.when(cinemaService.findCinemasBy(anyString(),anyInt(),anyBoolean(),anyLong(), any(Pageable.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas?name=\"Arena\"&open=true&halls=10&movieId=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].name", is("Arena")))
                .andExpect(jsonPath("$.content.[0].movies[0].title", is("Lord of the Rings")))
                .andReturn();
    }

    @Test
    public void testPutCinema() throws Exception {
        Cinema cinema = new Cinema("Cinema555", Boolean.TRUE, 10, "Address555");
        cinema.setId(555L);
        Optional<Cinema> optionalCinema = Optional.of(cinema);

        when(cinemaService.findById(555L)).thenReturn(optionalCinema);
        when(cinemaService.save(cinema)).thenReturn(cinema);

        Gson gson = new Gson();
        String gsonString = gson.toJson(cinema);

        mockMvc.perform(MockMvcRequestBuilders.put("/cinemas/555")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gsonString))
                .andExpect(status().isOk());

        verify(cinemaService, times(1)).findById(anyLong());
        verify(cinemaService, times(1)).save(cinema);
    }

    @Test
    public void testPutCinema_NoId() throws Exception {
        Cinema cinema = new Cinema("Cinema123123", Boolean.TRUE, 10, "Address123123");
        Optional<Cinema> nullOptionalCinema = Optional.empty();

        when(cinemaService.findById(555L)).thenReturn(nullOptionalCinema);
        when(cinemaService.save(any(Cinema.class))).thenReturn(cinema);

        Gson gson = new Gson();
        String gsonString = gson.toJson(cinema);

        mockMvc.perform(MockMvcRequestBuilders.put("/cinemas/555")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gsonString))
                .andExpect(status().isOk());

        verify(cinemaService, times(1)).findById(anyLong());
        verify(cinemaService, times(1)).save(any(Cinema.class));
    }

    @Test
    public void testDeleteCinema() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cinemas/123"))
                .andExpect(status().isOk());
        verify(cinemaService, only()).deleteById(123L);
    }

    @Test
    public void testSaveCinema() throws Exception {
        Cinema cinema = new Cinema("Cinegrand", Boolean.TRUE, 1000, "Nearby Mall");
        String cinemaGsonString = new Gson().toJson(cinema);
        when(cinemaService.save(any(Cinema.class))).thenReturn(cinema);

        mockMvc.perform(MockMvcRequestBuilders.post("/cinemas")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(cinemaGsonString))
                .andExpect(status().isOk());

        verify(cinemaService, times(1)).save(any(Cinema.class));
    }

}
