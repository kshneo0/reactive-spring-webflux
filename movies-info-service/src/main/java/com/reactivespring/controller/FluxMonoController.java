package com.reactivespring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxMonoController {

	@GetMapping("/flux")
	public Flux<Integer> flux(){
		return Flux.just(1,2,3).log();
	}
	
	@GetMapping("/mono")
	public Mono<String> helloWorldMono(){
		return Mono.just("hello-world").log();
	}
}
