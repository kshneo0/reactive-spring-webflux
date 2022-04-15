package com.reactivespring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.client.MoviesInfoRestClient;
import com.reactivespring.client.ReviewsRestClient;
import com.reactivespring.domain.Movie;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {
	
	private MoviesInfoRestClient moviesInfoRestClient;
	private ReviewsRestClient reviewsRestClient;

	public MoviesController(MoviesInfoRestClient moviesInfoRestClient, ReviewsRestClient reviewsRestClient) {
		super();
		this.moviesInfoRestClient = moviesInfoRestClient;
		this.reviewsRestClient = reviewsRestClient;
	}

	@GetMapping("/{id}")
	public Mono<Movie> reactiveMovieById(@PathVariable("id") String movieId){
		
		return moviesInfoRestClient.retrieveMovieInfo(movieId)
			.flatMap(movieInfo -> {
				
				var reviewsListMono = reviewsRestClient.retrieveReviewsById(movieId)
						.collectList();
				return reviewsListMono.map(reviews -> new Movie(movieInfo, reviews));
			});
			
	}
}
