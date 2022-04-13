package com.reactivespring.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = FluxMonoController.class)
@AutoConfigureWebTestClient
class FluxMonoControllerTest {
	
	@Autowired
	WebTestClient webTestClient;
	

	@Test
	void flux() {
		webTestClient.
			get()
			.uri("/flux")
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBodyList(Integer.class)
			.hasSize(3);
	}

}
