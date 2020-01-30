package com.softserveinc.demo;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.repository.CinemaRepository;
import com.softserveinc.demo.repository.MovieRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MentorshipDemoApplication implements CommandLineRunner {
	//demo runs on http://localhost:8080/

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private CinemaRepository cinemaRepository;

	public static void main(String[] args) {
		SpringApplication.run(MentorshipDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CreateMockData();
	}

	private void CreateMockData() {
		Movie harryPotter = movieRepository.save(new Movie("Harry Potter", Boolean.TRUE, 100));
		Movie lotr = new Movie("Lord of the Rings", Boolean.TRUE, 200);
		Movie garfield = new Movie("Garfield 5", Boolean.FALSE, 1);

		List<Movie> moviesList = new ArrayList<Movie>();
		moviesList.add(movieRepository.save(lotr));
		moviesList.add(movieRepository.save(garfield));

		Cinema cinemaOne = cinemaRepository.save(new Cinema("Arena", Boolean.TRUE, 10));
		Cinema cinemaTwo = cinemaRepository.save(new Cinema("IMAX", Boolean.FALSE, 5));

		cinemaOne.getMovies().add(harryPotter);
		cinemaTwo.getMovies().addAll(moviesList);
		cinemaRepository.save(cinemaOne);
		cinemaRepository.save(cinemaTwo);

	}
}
