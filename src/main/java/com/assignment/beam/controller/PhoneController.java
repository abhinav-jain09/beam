package com.assignment.beam.controller;

import com.assignment.beam.Entity.Phone;
import com.assignment.beam.response.PhoneDetailsResponse;
import com.assignment.beam.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/phones")
@Validated
public class PhoneController {
    private static final Logger logger = LoggerFactory.getLogger(PhoneController.class);
    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    @Operation(summary = "Get all phones", description = "Retrieve a list of all available phones")
    public Flux<Phone> getAllPhones() {
        return phoneService.getAllPhones();
    }

    @PostMapping("/{phoneName}/book")
    @Operation(summary = "Book a phone", description = "Book a phone by its name")
    public Mono<Phone> bookPhone(@PathVariable @NotBlank @Parameter(description = "Name of the phone") String phoneName,
                                 @RequestParam @NotBlank @Parameter(description = "Name of the person booking the phone") String bookedBy) {
        return phoneService.bookPhone(phoneName, bookedBy);
    }

    @GetMapping("/{phoneName}")
    @Operation(summary = "Get phone details", description = "Get details of a specific phone by name")
    public Mono<PhoneDetailsResponse> getPhoneDetailsByName(@PathVariable @NotBlank @Parameter(description = "Name of the phone") String phoneName) {
        return phoneService.getPhoneDetailsByName(phoneName);
    }

    @PostMapping("/{phoneName}/return")
    @Operation(summary = "Return a phone", description = "Return a previously booked phone")
    public Mono<Phone> returnPhone(@PathVariable @NotBlank @Parameter(description = "Name of the phone") String phoneName,
                                   @RequestParam @NotBlank @Parameter(description = "Name of the person returning the phone") String returnedBy) {
        return phoneService.returnPhone(phoneName, returnedBy);
    }
}
