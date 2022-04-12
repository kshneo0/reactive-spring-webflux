package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;

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
    
    public Flux<String> namesFlux_flatmap(int stringLength) {
    	// filter the string whose length is greater than 3
    	return Flux.fromIterable(List.of("alex", "ben", "chloe"))
    			.map(String::toUpperCase)
    			.filter(s -> s.length()>stringLength)	//4-ALEX, 5-CHOLE
    			//ALEX, CHOLE -> A, L, E, X, C, H, L, O, E
    			.flatMap(s -> splitString(s))	//A, L, E, X, C, H, L, O, E
    			.log(); // db or remote service
    	
    }
    
    public Flux<String> namesFlux_flatmap_async(int stringLength) {
    	// filter the string whose length is greater than 3
    	return Flux.fromIterable(List.of("alex", "ben", "chloe"))
    			.map(String::toUpperCase)
    			.filter(s -> s.length()>stringLength)	//4-ALEX, 5-CHOLE
    			//ALEX, CHOLE -> A, L, E, X, C, H, L, O, E
    			.flatMap(s -> splitString_withDelay(s))	//A, L, E, X, C, H, L, O, E
    			.log(); // db or remote service
    	
    }
    
    //ALEX -> Flux(A,L,E,X)
    public Flux<String> splitString(String name){
    	var charArray =  name.split("");
    	return Flux.fromArray(charArray);
    }
    
    public Flux<String> splitString_withDelay(String name){
    	var charArray =  name.split("");
    	var delay = new Random().nextInt(1000);
    	return Flux.fromArray(charArray)
    				.delayElements(Duration.ofMillis(delay));
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
