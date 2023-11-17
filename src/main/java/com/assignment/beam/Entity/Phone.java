package com.assignment.beam.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "bookedBy"})
public class Phone {

    @Schema(description = "Name of the phone", example = "iPhone 12")
    private String name;

    @Schema(description = "Availability of the phone", example = "true")
    private boolean available;

    @Schema(description = "Time when the phone was booked", example = "2021-01-01T12:00:00")
    private LocalDateTime bookedTime;

    @Schema(description = "Name of the person who booked the phone", example = "John Doe")
    private String bookedBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(name, phone.name) &&
                Objects.equals(bookedBy, phone.bookedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bookedBy);
    }
}
