package com.reactivespring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		return movieInfoService.getAllMovieInfos().log();
	}
	
	@GetMapping("/movieinfos/{id}")
	public Mono<MovieInfo> getMovieInfoById(@PathVariable String id){
		return movieInfoService.getMovieInfoById(id).log();
	}

	@PostMapping("/movieinfos")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo){
		return movieInfoService.addMovieInfo(movieInfo).log();
	}
	
	@PutMapping("/movieinfos/{id}")
	public Mono<MovieInfo> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id){
		return movieInfoService.updateMovieInfo(updatedMovieInfo, id).log();
	}
	
	@DeleteMapping("/movieinfos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteMovieInfo(@PathVariable String id){
		return movieInfoService.deleteMovieInfo(id).log();
	}

}
