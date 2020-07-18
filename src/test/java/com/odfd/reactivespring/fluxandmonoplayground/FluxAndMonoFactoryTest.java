package com.odfd.reactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "ana", "jack","jenny");

    @Test
    public void fluxUsingIterable(){
        Flux<String> namesFlux = Flux.fromIterable(names);

        StepVerifier.create(namesFlux)
            .expectNext("adam", "ana", "jack","jenny")
            .verifyComplete();
    }

    @Test
    public void fluxUsingArray(){
        Flux<String> namesFlux = Flux.fromArray(new String[]{"adam", "ana", "jack","jenny"});
//        Flux<String> namesFlux = Flux.fromArray((String[]) names.toArray());

        StepVerifier.create(namesFlux)
                .expectNext("adam", "ana", "jack","jenny")
                .verifyComplete();

    }

    @Test
    public void fluxUsingStream(){
        Flux<String> namesFlux = Flux.fromStream(names.stream());

        StepVerifier.create(namesFlux)
                .expectNext("adam", "ana", "jack","jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){

        Flux<Integer> integerFlux = Flux.range(1,5);

        StepVerifier.create(integerFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> monoString = Mono.justOrEmpty(null);

        StepVerifier.create(monoString.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Mono<String> monoString = Mono.fromSupplier(() -> "adam");

        StepVerifier.create(monoString)
                .expectNext("adam")
                .verifyComplete();
    }

}
