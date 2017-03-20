
package de.hrs.innovation.guests.tracking.apigateway.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author Susanna Ferrari
 */
@Data
@Accessors(chain = true)
public class ParcelInfo {

    private String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime acceptedDate;

}
