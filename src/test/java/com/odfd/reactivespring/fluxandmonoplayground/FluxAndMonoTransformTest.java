package com.odfd.reactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "ana", "jack","jenny");

    @Test
    public void transformUsingMap(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("ADAM", "ANA","JACK","JENNY")
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Length(){
        Flux<Integer> lengthsFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();

        StepVerifier.create(lengthsFlux)
                .expectNext(4,3,4,5)
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Length_Repeat(){
        Flux<Integer> lengthsFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .repeat(1)
                .log();

        StepVerifier.create(lengthsFlux)
                .expectNext(4,3,4,5,4,3,4,5)
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Filter(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s-> s.length() > 4)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("JENNY")
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap(){
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")) // A, B, C, D, E, F
                .flatMap(s -> Flux.fromIterable(convertToList(s))) // A-> List[A, newValue], db or external service call that returns a flux
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_UsingParallel(){
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")) // A, B, C, D, E, F
                .window(2) // Flux<Flux<String>> -> (A,B), (C,D)
                .flatMap((s) -> s.map(this::convertToList).subscribeOn(parallel())) // A-> List[A, newValue], db or external service call that returns a flux
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_parallel_maintain_order(){
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")) // A, B, C, D, E, F
                .window(2) // Flux<Flux<String>> -> (A,B), (C,D)
                //.concatMap((s) -> s.map(this::convertToList).subscribeOn(parallel())) // A-> List[A, newValue], db or external service call that returns a flux
                .flatMapSequential((s) -> s.map(this::convertToList).subscribeOn(parallel())) // A-> List[A, newValue], db or external service call that returns a flux
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }
}
