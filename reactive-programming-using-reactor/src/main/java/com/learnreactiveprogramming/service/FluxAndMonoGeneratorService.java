package com.learnreactiveprogramming.service;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {
	
    public Flux<String> namesFlux() {
        
        return Flux.fromIterable(List.of("alex", "ben", "chloe")); // coming from a db or remote service

    }
    
    public Mono<String> namesMono() {

        return Mono.just("alex");

    }
    
	public static void main(String[] args) {
		
		FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
		
		fluxAndMonoGeneratorService.namesFlux()
			.subscribe(name -> System.out.println("Flux Name is : " + name));
		
		fluxAndMonoGeneratorService.namesMono()
			.subscribe((name) -> System.out.println("Mono Name is : " + name));
		
	}
}
