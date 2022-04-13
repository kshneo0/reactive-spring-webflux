package com.learnreactiveprogramming.service;

import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    
    @Test
    void namesMono_flatMap() {

        //given
        int stringLength = 3;

        //when
        var values = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength).log();

        //then
        StepVerifier.create(values)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();

    }
    
    @Test
    void namesMono_flatMapMany() {
    	
    	//given
    	int stringLength = 3;
    	
    	//when
    	var values = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength).log();
    	
    	//then
    	StepVerifier.create(values)
    	.expectNext("A", "L", "E", "X")
    	.verifyComplete();
    	
    }
    
    @Test
    void namesFlux_transform() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .expectNextCount(5)
                .verifyComplete();

    }
    
    @Test
    void namesFlux_transform_1() {
    	
    	//given
    	int stringLength = 6;
    	
    	//when
    	var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength).log();
    	
    	//then
    	StepVerifier.create(namesFlux)
    	.expectNext("default")
    	.verifyComplete();
    	
    }
    
    @Test
    void namesFlux_transform_switchifEmpty() {
    	
    	//given
    	int stringLength = 6;
    	
    	//when
    	var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchifEmpty(stringLength).log();
    	
    	//then
    	StepVerifier.create(namesFlux)
    	.expectNext("D", "E", "F", "A", "U", "L", "T")
    	.verifyComplete();
    	
    }
    
    @Test
    void explore_concat() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_concat();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }
    

    @Test
    void explore_concatWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_concatWith();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }
    
    @Test
    void explore_concat_mono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_concatWith_mono();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B")
                .verifyComplete();

    }
    

    @Test
    void explore_merge() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_merge();

        //then
        StepVerifier.create(value)
                // .expectNext("A", "B", "C", "D", "E", "F")
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();

    }

    @Test
    void explore_mergeWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeWith();

        //then
        StepVerifier.create(value)

                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();

    }

    @Test
    void explore_mergeWith_mono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeWith_mono();

        //then
        StepVerifier.create(value)

                .expectNext("A", "B")
                .verifyComplete();

    }
    
    @Test
    void explore_mergeSequential() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeSequential();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }
    
    

}