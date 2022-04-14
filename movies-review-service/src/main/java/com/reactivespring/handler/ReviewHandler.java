package com.reactivespring.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;

import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {
	
	private ReviewReactiveRepository reviewReactiveRepository;

	public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
		this.reviewReactiveRepository = reviewReactiveRepository;
	}

	public Mono<ServerResponse> addReview(ServerRequest request) {
		return request.bodyToMono(Review.class)
			.flatMap(reviewReactiveRepository::save);
	}

}
