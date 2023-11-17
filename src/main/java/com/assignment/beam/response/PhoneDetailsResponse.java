package com.assignment.beam.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneDetailsResponse {
    private String availability;
    private LocalDateTime bookedTime;
    private String bookedBy;

}
