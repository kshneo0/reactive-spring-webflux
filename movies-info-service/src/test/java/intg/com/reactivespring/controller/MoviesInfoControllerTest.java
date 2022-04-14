package com.reactivespring.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MoviesInfoControllerTest {

	@Autowired
	MovieInfoRepository movieInfoRepository;
	
	@Autowired
	WebTestClient webTestClient; 
	
	static String MOVIE_INFO_URL = "/v1/movieinfos";
	
	@BeforeEach
	void setUp() throws Exception {
		var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
        new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
		
		movieInfoRepository.saveAll(movieinfos)
			.blockLast();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void addMovieInfo() {
		
		var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
		
		webTestClient
			.post()
			.uri(MOVIE_INFO_URL)
			.bodyValue(movieInfo)
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(MovieInfo.class)
			.consumeWith(movieInfoEntityExchangeResult -> {
				var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
				assert savedMovieInfo != null;
				assert savedMovieInfo.getMovieInfoId() != null;
			});
			
	}
	
	@Test
	void getAllMovieInfos() {
		
		webTestClient
			.get()
			.uri(MOVIE_INFO_URL)
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBodyList(MovieInfo.class)
			.hasSize(3);
		
	}

}
