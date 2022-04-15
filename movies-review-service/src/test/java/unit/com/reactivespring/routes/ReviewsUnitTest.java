package com.reactivespring.routes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.domain.Review;
import com.reactivespring.exceptionhandler.GlobalErrorHandler;
import com.reactivespring.handler.ReviewHandler;
import com.reactivespring.repository.ReviewReactiveRepository;
import com.reactivespring.router.ReviewRouter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalErrorHandler.class})
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
    
    @Test
    void addReview_Validations() {
        //given
        var review = new Review(null, null, "Awesome Movie", -9.0);
        
        when(reviewReactiveRepository.save(isA(Review.class)))
		.thenReturn(Mono.just(new Review("abc", 1L, "Awesome Movie", 9.0)));
        
        //when
        webTestClient
                .post()
                .uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("rating.movieInfoId : must not be null, rating.negative : please pass a non-negative value");


    }
    
    @Test
    void getAllReviews() {
        //given
        var reviewList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review(null, 2L, "Excellent Movie", 8.0));

        when(reviewReactiveRepository.findAll()).thenReturn(Flux.fromIterable(reviewList));

        //when
        webTestClient
                .get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .value(reviews -> {
                    assertEquals(3, reviews.size());
                });

    }
    
    @Test
    void updateReview() {
        //given

        var reviewUpdate = new Review(null, 1L, "Not an Awesome Movie", 8.0);

        when(reviewReactiveRepository.save(isA(Review.class))).thenReturn(Mono.just(new Review("abc", 1L, "Not an Awesome Movie", 8.0)));
        when(reviewReactiveRepository.findById((String) any())).thenReturn(Mono.just(new Review("abc", 1L, "Awesome Movie", 9.0)));
        //when


        webTestClient
                .put()
                .uri("/v1/reviews/{id}", "abc")
                .bodyValue(reviewUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class)
                .consumeWith(reviewResponse ->{
                    var updatedReview = reviewResponse.getResponseBody();
                    assert updatedReview != null;
                    System.out.println("updatedReview : "+ updatedReview);
                    assertEquals(8.0,updatedReview.getRating());
                    assertEquals("Not an Awesome Movie", updatedReview.getComment());
                });

    }
    
    @Test
    void deleteReview() {
        //given
        var reviewId= "abc";
        when(reviewReactiveRepository.findById((String) any())).thenReturn(Mono.just(new Review("abc", 1L, "Awesome Movie", 9.0)));
        when(reviewReactiveRepository.deleteById((String) any())).thenReturn(Mono.empty());

        //when
        webTestClient
                .delete()
                .uri("/v1/reviews/{id}", reviewId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
