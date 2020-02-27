package com.softserveinc.demo.service;

import com.softserveinc.demo.exception.EntityNotFoundException;
import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.repository.CinemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CinemaServiceTest {

    private CinemaService cinemaService;
    @Mock
    private CinemaRepository cinemaRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cinemaService = new CinemaServiceImpl(cinemaRepository);
    }

    @Test
    public void testFindAll() {
        List<Cinema> cinemas = new ArrayList<>();
        PageImpl<Cinema> pagedResponse = new PageImpl<>(cinemas);
        Mockito.when(cinemaRepository.findAll(any(PageRequest.class))).thenReturn(pagedResponse);

        Page<Cinema> result = cinemaService.findAll(PageRequest.of(0, 10));

        verify(cinemaRepository, times(1)).findAll(any(PageRequest.class));
        assertEquals(pagedResponse, result);
    }

    @Test
    public void testFindById() {
        Cinema cinema = new Cinema();
        Optional<Cinema> optionalCinema = Optional.of(cinema);
        when(cinemaRepository.findById((anyLong()))).thenReturn(optionalCinema);

        Optional<Cinema> result = cinemaService.findById(5L);

        verify(cinemaRepository, only()).findById(5L);
        assertEquals(optionalCinema, result);
    }

    @Test
    public void testGetById() {
        Cinema cinema = new Cinema();
        Optional<Cinema> optionalCinema = Optional.of(cinema);
        when(cinemaRepository.findById((anyLong()))).thenReturn(optionalCinema);

        Cinema result = cinemaService.getById(5L);

        verify(cinemaRepository, only()).findById(5L);
        assertEquals(cinema, result);
    }

    @Test
    public void testGetByIdNotExisting() {
        Long id = 88L;
        doThrow(new EntityNotFoundException("Cinema not found")).when(cinemaRepository).findById(any(Long.class));

        try {
            cinemaService.getById(id);
        } catch (EntityNotFoundException e) {
            assertEquals(e.getMessage(), "Cinema not found");
        }

        verify(cinemaRepository, only()).findById(id);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        cinemaService.deleteById(id);

        verify(cinemaRepository, only()).deleteById(id);
    }

    @Test
    public void testDeleteByIdNotExisting() {
        Long id = 999L;

        doThrow(new EmptyResultDataAccessException(1)).when(cinemaRepository).deleteById(any(Long.class));

        try {
            cinemaService.deleteById(id);
        } catch (EntityNotFoundException e) {
            assertTrue(e.getMessage().contains("Cinema not found"));
        }

        verify(cinemaRepository, only()).deleteById(id);
    }

    @Test
    public void testSaveCinema() {
        Cinema cinema = new Cinema();
        when(cinemaRepository.save(cinema)).thenReturn(cinema);

        Cinema result = cinemaService.save(cinema);

        verify(cinemaRepository, only()).save(cinema);
        assertEquals(cinema, result);
    }

}
