package com.assignment.beam;

import com.assignment.beam.controller.PhoneController;
import com.assignment.beam.Entity.Phone;
import com.assignment.beam.exceptions.PhoneNotAvailableException;
import com.assignment.beam.exceptions.PhoneNotFoundException;
import com.assignment.beam.response.PhoneDetailsResponse;
import com.assignment.beam.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)

@WebFluxTest(PhoneController.class)

public class PhoneControllerTest {

    @MockBean
    private PhoneService phoneService;
    @Autowired
    private WebTestClient webTestClient;

    @InjectMocks
    private PhoneController phoneController;

    @BeforeEach
    public void setUp() {
        sharedPhone = new Phone("Phone1", false, LocalDateTime.now(), "User1");
        MockitoAnnotations.openMocks(this);  // Ensure mocks are initialized
    }

    private Phone sharedPhone;

    @Test
    void getAllPhones_ShouldReturnPhones() {
        when(phoneService.getAllPhones()).thenReturn(Flux.just(/* Phone instances */));

        webTestClient.get().uri("/phones")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Phone.class);

        verify(phoneService).getAllPhones();
    }

    @Test
    void bookPhone_ShouldBookPhone() {
        when(phoneService.bookPhone("Phone1", "User1")).thenReturn(Mono.just(sharedPhone));

        webTestClient.post().uri("/phones/Phone1/book?bookedBy=User1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Phone.class);

        verify(phoneService).bookPhone("Phone1", "User1");
    }

    @Test
    void getPhoneDetailsByName_ShouldReturnPhoneDetails() {
        PhoneDetailsResponse phoneDetailsResponse = new PhoneDetailsResponse("Yes", LocalDateTime.now(), "User1");
        when(phoneService.getPhoneDetailsByName("Phone1")).thenReturn(Mono.just(phoneDetailsResponse));

        webTestClient.get().uri("/phones/Phone1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PhoneDetailsResponse.class)
                .isEqualTo(phoneDetailsResponse);

        verify(phoneService).getPhoneDetailsByName("Phone1");
    }


    @Test
    void returnPhone_ShouldReturnPhone() {
        when(phoneService.returnPhone("Phone1", "User1")).thenReturn(Mono.just(sharedPhone));

        webTestClient.post().uri("/phones/Phone1/return?returnedBy=User1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Phone.class);

        verify(phoneService).returnPhone("Phone1", "User1");
    }

    @Test
    void bookPhone_WhenNotAvailable_ShouldReturnError() {
        when(phoneService.bookPhone("Phone1", "User1"))
                .thenReturn(Mono.error(new PhoneNotAvailableException("Phone not available")));

        webTestClient.post().uri("/phones/Phone1/book?bookedBy=User1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody(String.class)
                .consumeWith(response ->
                        assertEquals("Phone not available", response.getResponseBody())
                );

        verify(phoneService).bookPhone("Phone1", "User1");
    }


    @Test
    void getPhoneDetailsByName_WhenNotFound_ShouldReturnError() {
        when(phoneService.getPhoneDetailsByName("Unknown"))
                .thenReturn(Mono.error(new PhoneNotFoundException("Phone not found")));

        webTestClient.get().uri("/phones/Unknown")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(response ->
                        assertEquals("Phone not found", response.getResponseBody())
                );

        verify(phoneService).getPhoneDetailsByName("Unknown");
    }


    @Test
    void returnPhone_WhenNotFound_ShouldReturnError() {
        when(phoneService.returnPhone("Unknown", "User1"))
                .thenReturn(Mono.error(new PhoneNotFoundException("Phone not found")));

        webTestClient.post().uri("/phones/Unknown/return?returnedBy=User1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(response ->
                        assertEquals("Phone not found", response.getResponseBody())
                );

        verify(phoneService).returnPhone("Unknown", "User1");
    }

}
