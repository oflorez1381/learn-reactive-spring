package com.odfd.reactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {

        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
                .log(); // starts from 0

        infiniteFlux
                .subscribe((element) -> System.out.println("Value " + element));

        Thread.sleep(3000);

    }

    @Test
    public void finiteSequence() {

        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log(); // starts from 0

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L,1L,2L)
                .verifyComplete();
    }

    @Test
    public void finiteSequenceMap() {

        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .map(Long::intValue)
                .take(3)
                .log(); // starts from 0

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }

    @Test
    public void finiteSequenceMap_withDelay() {

        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map(Long::intValue)
                .take(3)
                .log(); // starts from 0

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }

}
