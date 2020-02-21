package com.softserveinc.demo.controller;

import com.google.gson.Gson;
import com.softserveinc.demo.model.Cinema;
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
        newCinema.setId(0L);
        String JSON = new Gson().toJson(newCinema);

        headers.add("Content-Type", "application/json");
        headers.add("Accept","application/json");

        HttpEntity<String> entity = new HttpEntity<String>(JSON, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cinemas"), HttpMethod.POST, entity, String.class);

        String result = response.getHeaders().toString();

        //TODO assert
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}

