package com.reactivespring.controller;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinksTest {

	@Test
	void sink() {
		  Sinks.Many<Integer> replaySinks = Sinks.many().replay().all();
		  
		  replaySinks.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
		  replaySinks.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);
		  
		  Flux<Integer> integerFlux = replaySinks.asFlux();
		  
		  integerFlux
          .subscribe(s->{
              System.out.println("Subscriber 1 : " + s);
          });
		  
		  Flux<Integer> integerFlux1 = replaySinks.asFlux();

	      integerFlux1
	                .subscribe(s->{
	                    System.out.println("Subscriber 2 : " + s);
	                });
	      
	      replaySinks.tryEmitNext(3);
	      
	      Flux<Integer> integerFlux2 = replaySinks.asFlux();

	      integerFlux2
	                .subscribe(s->{
	                    System.out.println("Subscriber 3 : " + s);
	                });
		  
	}
	
    @Test
    void sink_multicast() throws InterruptedException {
        //given

        //when

        Sinks.Many<Integer> multiCast = Sinks.many().multicast().onBackpressureBuffer();

        multiCast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multiCast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        //then

        Flux<Integer> integerFlux = multiCast.asFlux();
        integerFlux
                .subscribe(i->{
                    System.out.println("Subscriber 1 : " + i);
                });

        Flux<Integer> integerFlux1 = multiCast.asFlux();

        integerFlux1
                .subscribe(i->{
                    System.out.println("Subscriber 2 : " + i);
                });
        
        multiCast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

    }
    
    @Test
    void sink_unicast() throws InterruptedException {
        //given

        //when

        Sinks.Many<Integer> multiCast = Sinks.many().unicast().onBackpressureBuffer();

        multiCast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multiCast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        //then

        Flux<Integer> integerFlux = multiCast.asFlux();
        integerFlux
                .subscribe(i->{
                    System.out.println("Subscriber 1 : " + i);
                });

        Flux<Integer> integerFlux1 = multiCast.asFlux();

        integerFlux1
                .subscribe(i->{
                    System.out.println("Subscriber 2 : " + i);
                });
        
        multiCast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
