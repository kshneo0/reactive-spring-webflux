package com.learnreactiveprogramming.service;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {
	
    public Flux<String> namesFlux() {
        
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        		.log(); // coming from a db or remote service

    }
    
    public Mono<String> namesMono() {

        return Mono.just("alex").log();

    }
    
    public Flux<String> namesFlux_map() {
    	
    	return Flux.fromIterable(List.of("alex", "ben", "chloe"))
    			.map(String::toUpperCase)
    			.log(); // coming from a db or remote service
    	
    }
    

    
	public static void main(String[] args) {
		
		FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
		
		fluxAndMonoGeneratorService.namesFlux()
			.subscribe(name -> System.out.println("Flux Name is : " + name));
		
		System.out.println("-------------------------------");
		
		fluxAndMonoGeneratorService.namesMono()
			.subscribe((name) -> System.out.println("Mono Name is : " + name));
		
	}
}
