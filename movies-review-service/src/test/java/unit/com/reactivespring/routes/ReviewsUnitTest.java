package com.reactivespring.routes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.domain.Review;
import com.reactivespring.handler.ReviewHandler;
import com.reactivespring.repository.ReviewReactiveRepository;
import com.reactivespring.router.ReviewRouter;

import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
@AutoConfigureWebTestClient
public class ReviewsUnitTest {
	
    @MockBean
    private ReviewReactiveRepository reviewReactiveRepository;

    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    void addReview() {
    	
    	var review = new Review(null, 1L, "Awesome Movie", 9.0);
    	
    	when(reviewReactiveRepository.save(isA(Review.class)))
    		.thenReturn(Mono.just(new Review("abc", 1L, "Awesome Movie", 9.0)));
    	
        webTestClient
	        .post()
	        .uri("/v1/reviews")
	        .bodyValue(review)
	        .exchange()
	        .expectStatus()
	        .isCreated()
	        .expectBody(Review.class)
	        .consumeWith(movieInfoEntityExchangeResult ->{
	            var savedReview = movieInfoEntityExchangeResult.getResponseBody();
	            assert savedReview != null;
	            assertNotNull(savedReview.getReviewId());
	            assertEquals("abc", savedReview.getReviewId());
	
	        });
    	
    	
    	
    }
}
