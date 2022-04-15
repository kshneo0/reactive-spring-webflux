package com.reactivespring.handler;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.exception.ReviewNotFoundException;
import com.reactivespring.repository.ReviewReactiveRepository;

import io.netty.channel.unix.Errors;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewHandler {
	
	@Autowired
    private Validator validator;
	
	private ReviewReactiveRepository reviewReactiveRepository;

	public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
		this.reviewReactiveRepository = reviewReactiveRepository;
	}

	public Mono<ServerResponse> addReview(ServerRequest request) {
		return request.bodyToMono(Review.class)
			.doOnNext(this::validate)
			.flatMap(reviewReactiveRepository::save)
			.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
	}
	
    private void validate(Review review) {

        var constraintViolations = validator.validate(review);
        log.info("constraintViolations : {} ", constraintViolations);
        if (constraintViolations.size() > 0) {
	        var errorMessage = constraintViolations.stream()
	                    .map(ConstraintViolation::getMessage)
	                    .sorted()
	                    .collect(Collectors.joining(", "));
	        
            throw new ReviewDataException(errorMessage);
        }
        
    }

	public Mono<ServerResponse> getReviews(ServerRequest request) {
		
		var movieInfoId = request.queryParam("movieInfoId");
		
		if(movieInfoId.isPresent()) {
			var reviewsFlux = reviewReactiveRepository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
			return buildReviewsResponse(reviewsFlux);
		} else {
			
			var reviewsFlux = reviewReactiveRepository.findAll();
			return buildReviewsResponse(reviewsFlux);
		}
	}

	private Mono<ServerResponse> buildReviewsResponse(Flux<Review> reviewsFlux) {
		return ServerResponse.ok().body(reviewsFlux, Review.class);
	}

	public Mono<ServerResponse> updateReview(ServerRequest request) {
		
		String reviewId = request.pathVariable("id");
		
		var existingReview = reviewReactiveRepository.findById(reviewId)
				.switchIfEmpty(Mono.error(new ReviewNotFoundException("Review not Found for the given Review Id " + reviewId)));
		
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
