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
    
    public Flux<String> namesFlux_map(int stringLength) {
    	// filter the string whose length is greater than 3
    	return Flux.fromIterable(List.of("alex", "ben", "chloe"))
    			.map(String::toUpperCase)
    			.filter(s -> s.length()>stringLength)	//4-ALEX, 5-CHOLE
    			.map(s -> s.length() + "-" + s)
    			.log(); // coming from a db or remote service
    	
    }
    
    public Flux<String> namesFlux_immutablity() {
    	
    	var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
    	
    	namesFlux.map(String::toUpperCase);
    	
    	return namesFlux;    	
    	
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
