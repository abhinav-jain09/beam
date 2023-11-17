package com.assignment.beam;

import com.assignment.beam.Entity.Phone;
import com.assignment.beam.controller.PhoneController;
import com.assignment.beam.exceptions.*;
import com.assignment.beam.repository.PhoneRepository;
import com.assignment.beam.response.PhoneDetailsResponse;
import com.assignment.beam.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
public class PhoneServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneService phoneService;

    private Phone phone1, phone2;

    @BeforeEach
    void setUp() {
        phone1 = new Phone("Phone1", true, null, null); // Replace with actual constructor
        phone2 = new Phone("Phone2", false, LocalDateTime.now(), "User1"); // Replace with actual constructor
    }

    @Test
    void getPhoneByName_ShouldReturnPhone() {
        when(phoneRepository.findByName("Phone1")).thenReturn(Flux.just(phone1));

        Mono<Phone> result = phoneService.getPhoneByName("Phone1");

        StepVerifier.create(result)
                .expectNext(phone1)
                .verifyComplete();

        verify(phoneRepository).findByName("Phone1");
    }

    @Test
    void getPhoneByName_WhenNotFound_ShouldReturnEmpty() {
        when(phoneRepository.findByName("Unknown")).thenReturn(Flux.empty());

        Mono<Phone> result = phoneService.getPhoneByName("Unknown");

        StepVerifier.create(result)
                .verifyComplete();

        verify(phoneRepository).findByName("Unknown");
    }

    @Test
    void getPhoneDetailsByName_ShouldReturnDetails() {
        when(phoneRepository.findByName("Phone1")).thenReturn(Flux.just(phone1));

        Mono<PhoneDetailsResponse> result = phoneService.getPhoneDetailsByName("Phone1");

        StepVerifier.create(result)
                .expectNextMatches(details -> details.getBookedBy() == null && details.getAvailability().equals("Yes"))
                .verifyComplete();

        verify(phoneRepository).findByName("Phone1");
    }

    @Test
    void bookPhone_ShouldBookPhone() {
        when(phoneRepository.findByName("Phone1")).thenReturn(Flux.just(phone1));

        Mono<Phone> result = phoneService.bookPhone("Phone1", "User2");

        StepVerifier.create(result)
                .expectNextMatches(phone -> phone.getBookedBy().equals("User2") && !phone.isAvailable())
                .verifyComplete();

        verify(phoneRepository).findByName("Phone1");
    }

    @Test
    void bookPhone_WhenNotAvailable_ShouldThrowException() {
        when(phoneRepository.findByName("Phone2")).thenReturn(Flux.just(phone2));

        Mono<Phone> result = phoneService.bookPhone("Phone2", "User2");

        StepVerifier.create(result)
                .expectError(PhoneNotAvailableException.class)
                .verify();

        verify(phoneRepository).findByName("Phone2");
    }

    @Test
    void getAllPhones_ShouldReturnAllPhones() {
        when(phoneRepository.findAll()).thenReturn(Flux.just(phone1, phone2));

        Flux<Phone> result = phoneService.getAllPhones();

        StepVerifier.create(result)
                .expectNext(phone1)
                .expectNext(phone2)
                .verifyComplete();

        verify(phoneRepository).findAll();
    }

    @Test
    void returnPhone_ShouldReturnPhone() {
        when(phoneRepository.findByName("Phone2")).thenReturn(Flux.just(phone2));

        Mono<Phone> result = phoneService.returnPhone("Phone2", "User1");

        StepVerifier.create(result)
                .expectNextMatches(phone -> phone.isAvailable() && phone.getBookedBy() == null)
                .verifyComplete();

        verify(phoneRepository).findByName("Phone2");
    }

    @Test
    void returnPhone_WhenIncorrectUser_ShouldThrowException() {
        when(phoneRepository.findByName("Phone2")).thenReturn(Flux.just(phone2));

        Mono<Phone> result = phoneService.returnPhone("Phone2", "User2");

        StepVerifier.create(result)
                .expectError(IncorrectReturnUserException.class)
                .verify();

        verify(phoneRepository).findByName("Phone2");
    }

    @Test
    void returnPhone_WhenAlreadyAvailable_ShouldThrowException() {
        phone1.setAvailable(true);  // Ensure the phone is already available
        phone1.setBookedBy("User1"); // Ensure the phone is booked by User1

        when(phoneRepository.findByName("Phone1")).thenReturn(Flux.just(phone1));

        Mono<Phone> result = phoneService.returnPhone("Phone1", "User1");

        StepVerifier.create(result)
                .expectError(PhoneAlreadyAvailableException.class)
                .verify();

        verify(phoneRepository).findByName("Phone1");
    }

    @Test
    void returnPhone_WhenNotFound_ShouldThrowException() {
        when(phoneRepository.findByName(anyString())).thenReturn(Flux.empty());

        Mono<Phone> result = phoneService.returnPhone("Unknown", "User1");

        StepVerifier.create(result)
                .expectError(PhoneNotFoundException.class)
                .verify();

        verify(phoneRepository).findByName(anyString());
    }

    @Test
    void bookPhone_WhenAllPhonesAlreadyBooked_ShouldThrowException() {
        String phoneName = "Samsung Galaxy S8";

        // Setting up two phones with the same name, both already booked
        Phone phone1 = new Phone(phoneName, false, LocalDateTime.now(), "User1");
        Phone phone2 = new Phone(phoneName, false, LocalDateTime.now(), "User2");

        when(phoneRepository.findByName(phoneName)).thenReturn(Flux.just(phone1, phone2));

        StepVerifier.create(phoneService.bookPhone(phoneName, "User3"))
                .expectError(PhoneNotAvailableException.class)
                .verify();
    }

}
