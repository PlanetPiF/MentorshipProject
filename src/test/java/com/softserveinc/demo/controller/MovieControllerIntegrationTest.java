package com.softserveinc.demo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softserveinc.demo.model.Movie;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerIntegrationTest {

    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testGetMovieById() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/movies/1"), HttpMethod.GET, entity, String.class);
        String expectedResult = "{ \"id\": 1, \"title\": \"Harry Potter\", \"released\": true," +
                " \"budget\": 100}";

        JSONAssert.assertEquals(expectedResult, response.getBody(), false);
    }

    @Test
    public void testGetAllMovies() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/movies"), HttpMethod.GET, entity, String.class);

        Type listType = new TypeToken<List<Movie>>(){}.getType();
        List<Movie> returnedMovies = new Gson().fromJson(response.getBody(), listType);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(returnedMovies);
        Assert.assertEquals(returnedMovies.size(), 3);
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}
