
package de.hrs.innovation.guests.tracking.apigateway.domain;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author Susanna Ferrari
 */
@Data
@Accessors(chain = true)
public class GuestInfo {

    /**
     * 
     * Defines the possible Guest statuses.
     */
    public enum GuestStatus {
        REGISTERED, CHECKED_IN, CHECKED_OUT
    }

    private Integer id;

    private String lastName;

    private String firstName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private GuestStatus status;

    private List<ParcelInfo> parcelsToPickUp;
}
