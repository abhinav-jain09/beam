package com.assignment.beam.repository;

import com.assignment.beam.Entity.Phone;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryPhoneRepository implements PhoneRepository {
    private final List<Phone> phones = new ArrayList<>();

    public InMemoryPhoneRepository() {
        phones.add(new Phone("Samsung Galaxy S9", true, null, null));
        phones.add(new Phone("Samsung Galaxy S8", true, null, null));
        phones.add(new Phone("Samsung Galaxy S8", true, null, null));
        phones.add(new Phone("Motorola Nexus 6", true, null, null));
        phones.add(new Phone("Oneplus 9", true, null, null));
        phones.add(new Phone("Apple iPhone 13", true, null, null));
        phones.add(new Phone("Apple iPhone 12", true, null, null));
        phones.add(new Phone("Apple iPhone 11", true, null, null));
        phones.add(new Phone("iPhone X", true, null, null));
        phones.add(new Phone("Nokia 3310", true, null, null));
    }

    @Override
    public Flux<Phone> findAll() {
        return Flux.fromIterable(phones);
    }

    public Flux<Phone> findByName(String name) {
        return Flux.fromStream(
                phones.stream()
                        .filter(phone -> phone.getName().equalsIgnoreCase(name))
        );
    }

    @Override
    public Mono<Phone> bookPhone(String name, String bookedBy) {
        return Mono.defer(() -> {
            return findByName(name)
                    .filter(Phone::isAvailable)  // Filter only available phones
                    .next()  // Take the first available phone
                    .map(phone -> {
                        phone.setAvailable(false);
                        phone.setBookedTime(LocalDateTime.now());
                        phone.setBookedBy(bookedBy);
                        return phone;
                    })
                    .switchIfEmpty(Mono.error(new RuntimeException("No available phones found.")));
        });
    }


    @Override
    public Mono<Phone> returnPhone(String name) {
        return Mono.defer(() -> {
            return findByName(name)
                    .filter(phone -> !phone.isAvailable())  // Filter out phones that are already available
                    .next()  // Take the first unavailable phone
                    .map(phone -> {
                        phone.setAvailable(true);
                        phone.setBookedTime(null);
                        phone.setBookedBy(null);
                        return phone;
                    })
                    .switchIfEmpty(Mono.error(new RuntimeException("No unavailable phones found with this name.")));
        });
    }

}
