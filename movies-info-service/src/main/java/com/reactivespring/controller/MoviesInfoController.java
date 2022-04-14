package com.reactivespring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.cservice.MovieInfoService;
import com.reactivespring.domain.MovieInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1")
public class MoviesInfoController {
	
	private MovieInfoService movieInfoService;

	public MoviesInfoController(MovieInfoService movieInfoService) {
		this.movieInfoService = movieInfoService;
	}
	
	@GetMapping("/movieinfos")
	public Flux<MovieInfo> getAllMovieInfos(){
		return movieInfoService.getAllMovieInfos();
	}

	@PostMapping("/movieinfos")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo){
		return movieInfoService.addMovieInfo(movieInfo).log();
	}

}
