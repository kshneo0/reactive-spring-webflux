package com.reactivespring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
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
			.onStatus(HttpStatus::is4xxClientError, clientResponse -> {
				log.info("Status code id : {}", clientResponse.statusCode().value() );
				if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
					return Mono.error(new MoviesInfoClientException(
							"There is no MovieInfo available for the passed in Id : "  + movieId, clientResponse.statusCode().value()
							));
				}
				
				return clientResponse.bodyToMono(String.class)
						.flatMap(responseMessage -> Mono.error(new MoviesInfoClientException(
								responseMessage, clientResponse.statusCode().value())));
			})
			.bodyToMono(MovieInfo.class)
			.log();
		
	}
}
