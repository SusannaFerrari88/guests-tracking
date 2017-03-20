
package de.hrs.innovation.guests.tracking.config;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 *
 * @author Susanna Ferrari
 */
@Getter
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    /**
     * Creates a UseCaseBadRequestException with the specified message and a "400 Bad Request"
     * status.
     * 
     * @param message
     *            the message
     */
    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
