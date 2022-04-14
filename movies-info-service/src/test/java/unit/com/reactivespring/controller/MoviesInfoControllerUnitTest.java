package com.reactivespring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	
	@Test
	void getMovieInfoById() {
	    var id = "abc";

	    when(moviesInfoServiceMock.getMovieInfoById(isA(String.class)))
	            .thenReturn(Mono.just(new MovieInfo("abc", "Dark Knight Rises",
	                    2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))));

	    webTestClient
	            .get()
	            .uri(MOVIES_INFO_URL + "/{id}", id)
	            .exchange()
	            .expectStatus()
	            .is2xxSuccessful()
	            /*.expectBody(MovieInfo.class)
	            .consumeWith(movieInfoEntityExchangeResult -> {
	                var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
	                assert movieInfo != null;
	            })*/
	            .expectBody()
	            .jsonPath("$.name").isEqualTo("Dark Knight Rises");

	}
	
	@Test
	void addMovieInfo() {
		
		var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
		
		when(moviesInfoServiceMock.addMovieInfo(isA(MovieInfo.class)))
         	.thenReturn(Mono.just( new MovieInfo("mockId", "Batman Begins1",
                    2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));
		
		webTestClient
			.post()
			.uri(MOVIES_INFO_URL)
			.bodyValue(movieInfo)
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(MovieInfo.class)
			.consumeWith(movieInfoEntityExchangeResult -> {
				var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
				assert savedMovieInfo != null;
				assert savedMovieInfo.getMovieInfoId() != null;
				assertEquals("mockId", savedMovieInfo.getMovieInfoId());
			});
			
	}
	
	@Test
	void updateMovieInfo() {
		
		var movieInfoId = "abc";
		var updateMovieInfo = new MovieInfo(null, "Dark Knight Rises1",
                2013, List.of("Christian Bale1", "Tom Hardy1"), LocalDate.parse("2012-07-20"));
		
		when(moviesInfoServiceMock.updateMovieInfo(isA(MovieInfo.class), isA(String.class)))
     	.thenReturn(Mono.just(updateMovieInfo));
		
		webTestClient
			.put()
			.uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
			.bodyValue(updateMovieInfo)
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBody(MovieInfo.class)
			.consumeWith(movieInfoEntityExchangeResult -> {
				var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
				assert movieInfo != null;
				assertEquals("Dark Knight Rises1", movieInfo.getName());
			});
			
	}
	
	@Test
	void updateMovieInfo_notfound() {
		
		var movieInfoId = "def";
		var updateMovieInfo = new MovieInfo(null, "Dark Knight Rises1",
                2013, List.of("Christian Bale1", "Tom Hardy1"), LocalDate.parse("2012-07-20"));
		
		  when(moviesInfoServiceMock.updateMovieInfo(isA(MovieInfo.class), isA(String.class)))
          .thenReturn(Mono.empty());
		
		webTestClient
			.put()
			.uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
			.bodyValue(updateMovieInfo)
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			;
			
	}
	
	@Test
	void deleteMovieInfoById() {
	    var id = "abc";

	    when(moviesInfoServiceMock.deleteMovieInfoById(isA(String.class)))
	            .thenReturn(Mono.empty());

	    webTestClient
	            .delete()
	            .uri(MOVIES_INFO_URL + "/{id}", id)
	            .exchange()
	            .expectStatus()
	            .isNoContent();
	}
	
	@Test
	void addMovieInfo_validation() {
		
		var movieInfo = new MovieInfo(null, "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));
		
		webTestClient
			.post()
			.uri(MOVIES_INFO_URL)
			.bodyValue(movieInfo)
			.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody(String.class)
			.consumeWith(stringEntityExchangeResult -> {
				var responseBody = stringEntityExchangeResult.getResponseBody();
				System.out.println("responseBody : " + responseBody);
				var expectedErrormessage = "movieInfo.cast must be present,movieInfo.name must be present,movieInfo.year must be Positive value";
				assert responseBody != null;
				assertEquals(expectedErrormessage,responseBody);
			})
			;
			
	}
	
	

}
