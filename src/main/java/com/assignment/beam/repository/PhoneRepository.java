package com.assignment.beam.repository;

import com.assignment.beam.Entity.Phone;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PhoneRepository {
    Flux<Phone> findAll();
    Flux<Phone> findByName(String name);

    Mono<Phone> bookPhone(String name, String bookedBy);
    Mono<Phone> returnPhone(String name);
}
