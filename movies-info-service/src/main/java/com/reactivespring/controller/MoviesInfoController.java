package com.reactivespring.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1")
@Slf4j
public class MoviesInfoController {
	
	private MovieInfoService movieInfoService;

	public MoviesInfoController(MovieInfoService movieInfoService) {
		this.movieInfoService = movieInfoService;
	}
	
	@GetMapping("/movieinfos")
	public Flux<MovieInfo> getAllMovieInfos(@RequestParam(value="year", required = false) Integer year){
		log.info("Year is : {}", year);
		
		if(year != null) {
			return movieInfoService.getMovieInfoByYear(year).log();
		}
		return movieInfoService.getAllMovieInfos().log();
	}
	
	@GetMapping("/movieinfos/{id}")
	public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id){
		return movieInfoService.getMovieInfoById(id)
				.map(movieInfo1 -> ResponseEntity.ok()
	                    .body(movieInfo1))
	            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
	            .log();
	}
	
	@PostMapping("/movieinfos")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo){
		return movieInfoService.addMovieInfo(movieInfo).log();
		
		// publish that movie to something
		// subscriber to this movie info
	}
	
	@PutMapping("/movieinfos/{id}")
	public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id){
		return movieInfoService.updateMovieInfo(updatedMovieInfo, id)
				.map(ResponseEntity.ok()::body)
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
				.log();
	}
	
	@DeleteMapping("/movieinfos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteMovieInfoById(@PathVariable String id){
		return movieInfoService.deleteMovieInfoById(id).log();
	}

}
