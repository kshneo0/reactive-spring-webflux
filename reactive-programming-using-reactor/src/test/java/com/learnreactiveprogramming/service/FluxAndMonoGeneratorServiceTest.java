package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;

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

}