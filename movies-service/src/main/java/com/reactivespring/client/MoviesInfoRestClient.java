package com.reactivespring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.reactivespring.domain.MovieInfo;

import reactor.core.publisher.Mono;

@Component
public class MoviesInfoRestClient {
	
	private WebClient webClient;
	
	@Value("${restClient.moviesInfoUrl}")
	private String movieInfoUrl;

	public MoviesInfoRestClient(WebClient webClient) {
		this.webClient = webClient;
	}
	
	public Mono<MovieInfo> retrieveMovieInfo(String movieId){
		
		var url = movieInfoUrl.concat("/{id}");
		return webClient
			.get()
			.uri(url, movieId)
			.retrieve()
			.bodyToMono(MovieInfo.class)
			.log();
		
	}
}
