package com.reactivespring.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactivespring.handler.ReviewHandler; 

@Configuration
public class ReviewRouter {
	
	@Bean
	public RouterFunction<ServerResponse> reviewRoutes(ReviewHandler reviewHandler){
		return route()
				.nest(path("/v1/reviews"), builder -> {
					builder.POST("",request -> reviewHandler.addReview(request))
							.GET("",request -> reviewHandler.getReviews(request))
							.PUT("/{id}", request -> reviewHandler.updateReview(request))
							.DELETE("/{id}",request -> reviewHandler.deleteReview(request));
				})
				.GET("/v1/helloworld",(request -> ServerResponse.ok().bodyValue("HelloWorld")))
//				.POST("/v1/reviews",request -> reviewHandler.addReview(request))
//				.GET("/v1/reviews",request -> reviewHandler.getReviews(request))
				.build();
	}
}
