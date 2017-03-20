
package de.hrs.innovation.guests.tracking.guest.domain;

import java.time.LocalDate;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author Susanna Ferrari
 */
@Data
@Accessors(chain = true)
public class GuestCreateRequest {

    @NotBlank(message = "lastName must not be blank")
    private String lastName;

    @NotBlank(message = "firstName must not be blank")
    private String firstName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private boolean checkIn;
}
