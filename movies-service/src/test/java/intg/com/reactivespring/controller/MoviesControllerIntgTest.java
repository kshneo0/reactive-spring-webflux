package com.reactivespring.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.domain.Movie;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebClient
@AutoConfigureWireMock(port = 8084) // automaticaly spins up a httpserver in port 8084
@TestPropertySource(properties = {
        "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
        "restClient.reviewsUrl=http://localhost:8084/v1/reviews",
})
public class MoviesControllerIntgTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	@Test
	void retrieveMovieById() {
		
		var movieId = "abc";
		stubFor(get(urlEqualTo("/v1/movieinfos"+ "/" + movieId))
				.willReturn(aResponse()
				.withHeader("Content-Type", "applicaytion/json")
				.withBodyFile("movieinfo.json")));
		
        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")));
		
		webTestClient
			.get()
			.uri("/v1/movie/{id}", movieId)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Movie.class)
			.consumeWith(movieEntityExchangeResult -> {
				var movie = movieEntityExchangeResult.getResponseBody();
				assert Objects.requireNonNull(movie).getReviewList().size()==2;
				assertEquals("Batman Begins", movie.getMovieInfo().getName());				
			});
		
	}
}
