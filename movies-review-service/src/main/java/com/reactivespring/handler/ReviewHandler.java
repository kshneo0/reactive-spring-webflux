package com.reactivespring.handler;

import org.springframework.http.HttpStatus;
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
			.flatMap(reviewReactiveRepository::save)
			.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
	}

	public Mono<ServerResponse> getReviews(ServerRequest request) {
		var reviewsFlux = reviewReactiveRepository.findAll();
		return ServerResponse.ok().body(reviewsFlux, Review.class);
	}

	public Mono<ServerResponse> updateReview(ServerRequest request) {
		
		String reviewId = request.pathVariable("id");
		var existingReview = reviewReactiveRepository.findById(reviewId);
		return existingReview
				.flatMap(review -> request.bodyToMono(Review.class)
					.map(reqReview -> {
						review.setComment(reqReview.getComment());
						review.setRating(reqReview.getRating());
						return review;
					})
					.flatMap(reviewReactiveRepository::save)
					.flatMap(ServerResponse.ok()::bodyValue));
		
	}

	public Mono<ServerResponse> deleteReview(ServerRequest request) {
		String reviewId = request.pathVariable("id");
		var existingReview = reviewReactiveRepository.findById(reviewId);
		return existingReview
				.flatMap(review -> reviewReactiveRepository.deleteById(reviewId))
				.then(ServerResponse.noContent().build())
				;
	}

}
