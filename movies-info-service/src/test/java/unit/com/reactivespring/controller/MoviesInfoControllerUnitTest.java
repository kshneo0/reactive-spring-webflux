package com.reactivespring.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.cservice.MovieInfoService;
import com.reactivespring.domain.MovieInfo;

import reactor.core.publisher.Flux;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerUnitTest {
	
	static String MOVIES_INFO_URL = "/v1/movieinfos";

	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean 
	private MovieInfoService moviesInfoServiceMock;
	
	@Test
	void getAllMoviesInfo() {
		
		var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
        new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
		
		when(moviesInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieinfos));
		
		webTestClient
		.get()
		.uri(MOVIES_INFO_URL)
		.exchange()
		.expectStatus()
		.is2xxSuccessful()
		.expectBodyList(MovieInfo.class)
		.hasSize(3);
		
	}
	

}
