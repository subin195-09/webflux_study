package com.example.fluxdemo.web;

import com.example.fluxdemo.domain.Customer;
import com.example.fluxdemo.domain.CustomerRepository;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.awt.*;
import java.time.Duration;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final Sinks.Many<Customer> sink;

    // A 요청 -> Flux -> Stream
    // B 요청 -> Flux -> Stream
    // -> Flux.merge -> sink
    // A요청의 flux와 B요청의 flux가 있는데, 이 둘을 합쳐주는 것(merge)을 sink 라고 한다.

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.sink = Sinks.many().multicast().onBackpressureBuffer(); // 새로 push 된 데이터 구독자에게 전송해주는 방식
    }

    // 데이터가 시간을 두고 onNext()를 호출 하는 것을 볼 수 있다.
    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxstream() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    // 데이터가 여러개일 때는 Flux를 쓴다.
    // request(unbounded) 는 최대한 많이 가져오라는 의미이다.
    // 만약 request(1) 이라면 데이터를 최대 1개까지만 가져오라는 의미가 된다.
    @GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Customer> findAll() {
        return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    // 데이터가 한 개 일 때는 Mono를 쓴다.
    @GetMapping("/customer/{id}")
    public Mono<Customer> findById(@PathVariable Long id) {
        return customerRepository.findById(id).log();
    }

    // 데이터를 모두 받아도 응답을 대기 한다.
    // 합쳐진 데이터가 없다면 계속 대기 상태가 된다.
    @GetMapping(value = "/customer/sse") // 생략 produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public Flux<ServerSentEvent<Customer>> findAllSSE() {
        return sink.asFlux().map(c -> ServerSentEvent.builder(c).build());
    }

    // 새로운 데이터 생성
    // curl -X POST http://localhost:8080/customer
    // /customer/sse 에서 데이터가 실시간으로 추가되는 것을 볼 수 있다.
    @PostMapping("/customer")
    public Mono<Customer> save() {
        return customerRepository.save(new Customer("soobin", "kim")).doOnNext(c -> {
            sink.tryEmitNext(c);
        });
    }
}
