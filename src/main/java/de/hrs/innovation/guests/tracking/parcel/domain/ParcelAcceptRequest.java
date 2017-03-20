
package de.hrs.innovation.guests.tracking.parcel.domain;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 *
 * @author Susanna Ferrari
 */
@Data
public class ParcelAcceptRequest {

    @NotBlank(message = "code must not be blank")
    private String code;

    @NotBlank(message = "guestId must not be blank")
    private Integer guestId;
}
