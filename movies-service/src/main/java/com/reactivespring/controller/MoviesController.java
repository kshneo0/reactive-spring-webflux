package com.reactivespring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.domain.Movie;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

	@GetMapping("/{id}")
	public Mono<Movie> reactiveMovieById(@PathVariable("id") String movieId){
		return null;
	}
}
