package com.reactivespring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.reactivespring.domain.Review;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

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
			.bodyToFlux(Review.class);
		
	}
	
}
