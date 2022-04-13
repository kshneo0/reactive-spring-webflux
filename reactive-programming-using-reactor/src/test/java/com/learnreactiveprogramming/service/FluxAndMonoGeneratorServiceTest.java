package com.learnreactiveprogramming.service;

import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {
	
	FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
	
    @Test
    void namesFlux() {

        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();

    }
    
    @Test
    void namesFlux_map() {

        //given
        
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(3).log();

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("ALEX", "BEN", "CHLOE")
//        		.expectNext("ALEX", "CHLOE")
        		.expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();

    }
    
    @Test
    void namesFlux_Immutability() {

        //given

        //when
        var stringFlux = fluxAndMonoGeneratorService.namesFlux_immutablity()
                .log();

        //then
        StepVerifier.create(stringFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
        		.expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .verifyComplete();


    }
    
    @Test
    void namesFlux_flatmap() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }
    
    @Test
    void namesFlux_flatmap_async() {
    	
    	//given
    	int stringLength = 3;
    	
    	//when
    	var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength).log();
    	
    	//then
    	StepVerifier.create(namesFlux)
    	.expectNextCount(9)
    	.verifyComplete();
    	
    }
    
    @Test
    void namesFlux_concatmap() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

}