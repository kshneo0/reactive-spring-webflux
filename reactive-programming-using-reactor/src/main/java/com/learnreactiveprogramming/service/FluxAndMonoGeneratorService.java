package com.learnreactiveprogramming.service;

import java.util.List;

import reactor.core.publisher.Flux;

public class FluxAndMonoGeneratorService {
	
    public Flux<String> namesFlux() {
//        var namesList = List.of("alex", "ben", "chloe");
        //return Flux.just("alex", "ben", "chloe");
//        return Flux.fromIterable(namesList); // coming from a db or remote service
        
        return Flux.fromIterable(List.of("alex", "ben", "chloe")); // coming from a db or remote service

    }
    
	public static void main(String[] args) {
		
		FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
		
		fluxAndMonoGeneratorService.namesFlux()
			.subscribe(name -> System.out.println("Name is : " + name));
		
	}
}
