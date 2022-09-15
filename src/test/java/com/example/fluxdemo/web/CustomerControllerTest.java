package com.example.fluxdemo.web;

import com.example.fluxdemo.domain.Customer;
import com.example.fluxdemo.domain.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

//@WebFluxTest
//@SpringBootTest

@WebFluxTest
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webClient; // 비동기로 http 요청

    @MockBean
    CustomerRepository customerRepository;

    @Test
    public void findById_test() {

        // given
        Mono<Customer> givenData = Mono.just(new Customer("Jack", "Bauer"));

        // stub -> 행동 지시
        when(customerRepository.findById(1L))
                .thenReturn(givenData);

        webClient.get().uri("/customer/{id}", 1L)
                .exchange()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Jack")
                .jsonPath("$.lastName").isEqualTo("Bauer");

    }
}
