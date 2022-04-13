package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
        
    public Mono<String> namesMono_map_filter(int stringLength) {

        return Mono.just("alex")
        		.map(String::toUpperCase)
        		.filter(s -> s.length() > stringLength);

    }
    
    public Mono<List<String>> namesMono_flatMap(int stringLength) {
    	
    	return Mono.just("alex")
    			.map(String::toUpperCase)
    			.filter(s -> s.length() > stringLength)
    			.flatMap(this::splitStringMono);	//Mono<list of A, L, E, X>
    	
    }
    
    public Flux<String> namesMono_flatMapMany(int stringLength) {
    	
    	return Mono.just("alex")
    			.map(String::toUpperCase)
    			.filter(s -> s.length() > stringLength)
    			.flatMapMany(this::splitString)
    			.log();
    	
    }
    
    private Mono<List<String>> splitStringMono(String s) {
    	var charArray = s.split("");
    	var charList = List.of(charArray);	//ALEX -> A, L, E, X
    	return Mono.just(charList);
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
    
    public Flux<String> namesFlux_concatmap(int stringLength) {
    	// filter the string whose length is greater than 3
    	return Flux.fromIterable(List.of("alex", "ben", "chloe"))
    			.map(String::toUpperCase)
    			.filter(s -> s.length()>stringLength)	//4-ALEX, 5-CHOLE
    			//ALEX, CHOLE -> A, L, E, X, C, H, L, O, E
    			.concatMap(s -> splitString_withDelay(s))	//A, L, E, X, C, H, L, O, E
    			.log(); // db or remote service
    	
    }
    
    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        var namesList = List.of("alex", "ben", "chloe"); // a, l, e , x
        return Flux.fromIterable(namesList)
                .transform(filterMap) // gives u the opportunity to combine multiple operations using a single call.
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    	
    }
    
    public Flux<String> namesFlux_transform_switchifEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);
        
        var defaultFlux = Flux.just("default")
                .transform(filterMap); //"D","E","F","A","U","L","T"
        

        var namesList = List.of("alex", "ben", "chloe"); // a, l, e , x
        return Flux.fromIterable(namesList)
                .transform(filterMap) // gives u the opportunity to combine multiple operations using a single call.
                .switchIfEmpty(defaultFlux)
                .log();
        //using "map" would give the return type as Flux<Flux<String>
    }
    
    // "A", "B", "C", "D", "E", "F"
    public Flux<String> explore_concat() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux).log();

    }
    
    // "A", "B", "C", "D", "E", "F"
    public Flux<String> explore_concatWith() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();

    }
    
    public Flux<String> explore_concatWith_mono() {

        var aMono = Mono.just("A");

        var bMono = Flux.just("B");

        return aMono.concatWith(bMono);

    }
    
    // "A", "D", "B", "E", "C", "F"
    // Flux is subscribed early
    public Flux<String> explore_merge() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux).log();


    }

    // "A", "D", "B", "E", "C", "F"
    // Flux is subscribed early
    public Flux<String> explore_mergeWith() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();


    }

    public Flux<String> explore_mergeWith_mono() {

        var aMono = Mono.just("A");

        var bMono = Flux.just("B");

        return aMono.mergeWith(bMono);


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
