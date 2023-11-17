package com.assignment.beam.service;

import com.assignment.beam.Entity.Phone;
import com.assignment.beam.exceptions.IncorrectReturnUserException;
import com.assignment.beam.exceptions.PhoneAlreadyAvailableException;
import com.assignment.beam.exceptions.PhoneNotAvailableException;
import com.assignment.beam.exceptions.PhoneNotFoundException;
import com.assignment.beam.repository.PhoneRepository;
import com.assignment.beam.response.PhoneDetailsResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PhoneService {
    private final PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public Mono<Phone> getPhoneByName(String phoneName) {
        return phoneRepository.findByName(phoneName).next();
    }

    public Mono<PhoneDetailsResponse> getPhoneDetailsByName(String phoneName) {
        return getPhoneByName(phoneName)
                .map(this::mapToPhoneDetailsResponse);
    }

    public Mono<Phone> bookPhone(String phoneName, String bookedBy) {
        return phoneRepository.findByName(phoneName)
                .collectList()
                .flatMap(phones -> {
                    long availableCount = phones.stream().filter(Phone::isAvailable).count();
                    if (availableCount > 0) {
                        Phone phoneToBook = phones.stream().filter(Phone::isAvailable).findFirst().orElse(null);
                        if (phoneToBook != null) {
                            phoneToBook.setAvailable(false);
                            phoneToBook.setBookedTime(LocalDateTime.now());
                            phoneToBook.setBookedBy(bookedBy);
                            return Mono.just(phoneToBook);
                        }
                    }
                    return Mono.error(new PhoneNotAvailableException("Phone is not available for booking."));
                });
    }

    public Flux<Phone> getAllPhones() {
        return phoneRepository.findAll();
    }

    public Mono<Phone> returnPhone(String phoneName, String returnedBy) {
        return getPhoneByName(phoneName)
                .switchIfEmpty(Mono.error(new PhoneNotFoundException("Phone not found: " + phoneName)))
                .flatMap(phone -> {
                    if (!phone.isAvailable() && returnedBy.equals(phone.getBookedBy())) {
                        phone.setAvailable(true);
                        phone.setBookedTime(null);
                        phone.setBookedBy(null);
                        return Mono.just(phone);
                    } else if (!returnedBy.equals(phone.getBookedBy())) {
                        return Mono.error(new IncorrectReturnUserException("This phone was not booked by " + returnedBy));
                    } else {
                        return Mono.error(new PhoneAlreadyAvailableException("Phone is already available."));
                    }
                });
    }


    private PhoneDetailsResponse mapToPhoneDetailsResponse(Phone phone) {
        return new PhoneDetailsResponse(
                phone.isAvailable() ? "Yes" : "No",
                phone.getBookedTime(),
                phone.getBookedBy()
        );
    }
}
