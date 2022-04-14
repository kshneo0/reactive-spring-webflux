package com.reactivespring.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.reactivespring.handler.ReviewHandler; 

@Configuration
public class ReviewRouter {
	
	@Bean
	public RouterFunction<ServerResponse> reviewRoutes(ReviewHandler reviewHandler){
		return route()
				.GET("/v1/helloworld",(request -> ServerResponse.ok().bodyValue("HelloWorld")))
				.POST("/v1/reviews",request -> reviewHandler.addReview(request))
				.GET("/v1/reviews",request -> reviewHandler.getReviews(request))
				.build();
	}
}
