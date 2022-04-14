package com.reactivespring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.reactivespring.domain.MovieInfo;

import reactor.test.StepVerifier;

@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MovieInfoRepositoryIntgTest {

	@Autowired
	MovieInfoRepository movieInfoRepository;
	
	@BeforeEach
	void setUp() {
		
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
	void tearDown() {
		movieInfoRepository.deleteAll().block();
	}
	
	@Test
	void findAll() {
		var moviesInfoFlux = movieInfoRepository.findAll();
		
		StepVerifier.create(moviesInfoFlux)
			.expectNextCount(3)
			.verifyComplete();
		
	}
	
	@Test
	void findById() {
		var moviesInfoMono = movieInfoRepository.findById("abc").log();
		
		StepVerifier.create(moviesInfoMono)
		//.expectNextCount(1)
		.assertNext(movieInfo -> {
			assertEquals("Dark Knight Rises",movieInfo.getName());
		})
		.verifyComplete();
		
	}

}