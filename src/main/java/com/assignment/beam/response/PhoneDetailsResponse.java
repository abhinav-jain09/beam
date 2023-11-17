package com.assignment.beam.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@AllArgsConstructor
@Data
public class PhoneDetailsResponse {
    private String availability;
    private LocalDateTime bookedTime;
    private String bookedBy;

}
