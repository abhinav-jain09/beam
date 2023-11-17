package com.assignment.beam;


import com.assignment.beam.Entity.Phone;
import com.assignment.beam.repository.InMemoryPhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class InMemoryPhoneRepositoryTest {

    private InMemoryPhoneRepository inMemoryPhoneRepository;

    @BeforeEach
    void setUp() {
        inMemoryPhoneRepository = new InMemoryPhoneRepository();
    }

    @Test
    void findAll_ShouldReturnAllPhones() {
        StepVerifier.create(inMemoryPhoneRepository.findAll())
                .expectNextCount(10) // Assuming 10 phones in the repository
                .verifyComplete();
    }

    @Test
    void findByName_ExistingName_ShouldReturnMatchingPhones() {
        String name = "Samsung Galaxy S8";
        StepVerifier.create(inMemoryPhoneRepository.findByName(name))
                .expectNextMatches(phone -> phone.getName().equalsIgnoreCase(name))
                .expectNextMatches(phone -> phone.getName().equalsIgnoreCase(name))
                .verifyComplete();
    }

    @Test
    void findByName_NonExistingName_ShouldReturnEmpty() {
        String name = "Non Existing";
        StepVerifier.create(inMemoryPhoneRepository.findByName(name))
                .verifyComplete();
    }

    @Test
    void bookPhone_AvailablePhone_ShouldBookPhone() {
        String phoneName = "Samsung Galaxy S9";
        String bookedBy = "User1";
        StepVerifier.create(inMemoryPhoneRepository.bookPhone(phoneName, bookedBy))
                .expectNextMatches(phone -> phone.getName().equalsIgnoreCase(phoneName) && !phone.isAvailable() && phone.getBookedBy().equals(bookedBy))
                .verifyComplete();
    }

    @Test
    void bookPhone_UnavailablePhone_ShouldThrowException() {
        // First book the phone
        inMemoryPhoneRepository.bookPhone("Samsung Galaxy S9", "User1").block();

        // Try booking again
        StepVerifier.create(inMemoryPhoneRepository.bookPhone("Samsung Galaxy S9", "User2"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void returnPhone_UnavailablePhone_ShouldReturnPhone() {
        // First book the phone
        String phoneName = "Motorola Nexus 6";
        inMemoryPhoneRepository.bookPhone(phoneName, "User1").block();

        // Then return it
        StepVerifier.create(inMemoryPhoneRepository.returnPhone(phoneName))
                .expectNextMatches(phone -> phone.getName().equalsIgnoreCase(phoneName) && phone.isAvailable() && phone.getBookedBy() == null)
                .verifyComplete();
    }

    @Test
    void returnPhone_AlreadyReturnedPhone_ShouldThrowException() {
        String phoneName = "Apple iPhone 13";

        StepVerifier.create(inMemoryPhoneRepository.returnPhone(phoneName))
                .expectError(RuntimeException.class)
                .verify();
    }
}
