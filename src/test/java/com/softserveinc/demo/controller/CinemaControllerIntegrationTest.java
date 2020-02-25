package com.softserveinc.demo.controller;

import com.google.gson.Gson;
import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CinemaControllerIntegrationTest {
    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testGetCinema() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cinemas/5"), HttpMethod.GET, entity, String.class);
        String cinema5 = "{ \"id\": 5, \"name\": \"Arena\", \"halls\": 20," +
                " \"address\": \"Address5\", \"movies\": [], \"open\": true }";
        String result = response.getBody();

        JSONAssert.assertEquals(cinema5, result, false);
    }

    @Test
    public void testPostCinema() throws Exception {
        Cinema newCinema = new Cinema("TestName",Boolean.TRUE,22,"TestAddress");
        String JSON = new Gson().toJson(newCinema);

        headers.add("Content-Type", "application/json");
        headers.add("Accept","application/json");

        HttpEntity<String> entity = new HttpEntity<String>(JSON, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cinemas"), HttpMethod.POST, entity, String.class);

        Cinema cinemaFromResponse = new Gson().fromJson(response.getBody(), Cinema.class);

        assertEquals(newCinema.getName(), cinemaFromResponse.getName());
        assertEquals(newCinema.getAddress(), cinemaFromResponse.getAddress());
        assertEquals(newCinema.isOpen(), cinemaFromResponse.isOpen());
        assertEquals(newCinema.getHalls(), cinemaFromResponse.getHalls());
        assertEquals(newCinema.getMovies().size(), cinemaFromResponse.getMovies().size());
    }

    @Test
    public void testPutCinema() throws JSONException {
        // Create new Cinema and parse it into JSON
        Cinema newCinema = new Cinema("PutMeIn",Boolean.TRUE,222,"CinemaStreet");
        newCinema.setId(4L);
        Movie newMovie = new Movie("Test of the rings", Boolean.TRUE, 200);
        newCinema.getMovies().add(newMovie);
        String JSON = new Gson().toJson(newCinema);

        // Add Request Headers to use JSON
        headers.add("Content-Type", "application/json");
        headers.add("Accept","application/json");

        // Update (PUT) the cinema
        HttpEntity<String> entity = new HttpEntity<String>(JSON, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(
                createURLWithPort("/cinemas/4"), HttpMethod.PUT, entity, String.class);
        assertTrue(putResponse.toString().startsWith("<200"));

        // Compare the original cinema with the reponse
        Cinema cinemaFromResponse = new Gson().fromJson(putResponse.getBody(), Cinema.class);
        assertEquals(newCinema.getId(), cinemaFromResponse.getId());
        assertEquals(newCinema.getName(), cinemaFromResponse.getName());
        assertEquals(newCinema.getAddress(), cinemaFromResponse.getAddress());
        assertEquals(newCinema.isOpen(), cinemaFromResponse.isOpen());
        assertEquals(newCinema.getHalls(), cinemaFromResponse.getHalls());
        assertEquals(newCinema.getMovies().size(), cinemaFromResponse.getMovies().size());
    }

    @Test
    public void testDeleteExistingCinema() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        // Try successful delete on existing ID
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cinemas/4"), HttpMethod.DELETE, entity, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testDeleteNonExistingCinema() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        // Try deleting Cinema with ID that doesn't exist
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cinemas/555"), HttpMethod.DELETE, entity, String.class);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}

