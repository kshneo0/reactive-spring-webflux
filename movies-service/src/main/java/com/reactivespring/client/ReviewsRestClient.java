package com.reactivespring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.reactivespring.domain.Review;

import com.reactivespring.exception.ReviewsClientException;
import com.reactivespring.exception.ReviewsServerException;
import com.reactivespring.util.RetryUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewsRestClient {

	private WebClient webClient;
	
	@Value("${restClient.reviewsUrl}")
	private String reviewsUrl;

	public ReviewsRestClient(WebClient webClient) {
		this.webClient = webClient;
	}
	
	//movieInfoId
	public Flux<Review> retrieveReviewsById(String movieId){
		
		var url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
			.queryParam("movieInfoId", movieId)
			.buildAndExpand().toUriString();
		
		return webClient
			.get()
			.uri(url)
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, clientResponse -> {
				log.info("Status code id : {}", clientResponse.statusCode().value() );
				if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
					return Mono.empty();
				}				
				return clientResponse.bodyToMono(String.class)
						.flatMap(responseMessage -> Mono.error(new ReviewsClientException(
								responseMessage)));
			})
			.onStatus(HttpStatus::is5xxServerError, clientResponse -> {
				log.info("Status code id : {}", clientResponse.statusCode().value() );
				
				return clientResponse.bodyToMono(String.class)
						.flatMap(responseMessage -> Mono.error(new ReviewsServerException(
								"Server Exception in ReviewsService " + responseMessage)));
								
			})
			.bodyToFlux(Review.class)
			.retryWhen(RetryUtil.retrySpec());
		
	}
	
}
