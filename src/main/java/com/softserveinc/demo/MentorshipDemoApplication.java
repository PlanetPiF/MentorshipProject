package com.softserveinc.demo;

import com.softserveinc.demo.model.Cinema;
import com.softserveinc.demo.model.Movie;
import com.softserveinc.demo.repository.CinemaRepository;
import com.softserveinc.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		Cinema cinemaOne = cinemaRepository.save(new Cinema("Arena", Boolean.TRUE, 10, "Address4"));
		Cinema cinemaOne1 = cinemaRepository.save(new Cinema("Arena", Boolean.TRUE, 20, "Address5"));
		Cinema cinemaOne2 = cinemaRepository.save(new Cinema("Arena", Boolean.TRUE, 30, "Address6"));
		Cinema cinemaOne3 = cinemaRepository.save(new Cinema("Arena", Boolean.TRUE, 40, "Address7"));
		Cinema cinemaTwo = cinemaRepository.save(new Cinema("IMAX", Boolean.FALSE, 5, "Address8"));

		cinemaOne.getMovies().add(harryPotter);
		cinemaTwo.getMovies().addAll(moviesList);
		cinemaRepository.save(cinemaOne);
		cinemaRepository.save(cinemaTwo);

	}
}
