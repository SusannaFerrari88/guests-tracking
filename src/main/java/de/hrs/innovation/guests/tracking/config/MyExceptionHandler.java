
package de.hrs.innovation.guests.tracking.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Susanna Ferrari
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @Getter
    @AllArgsConstructor
    class ErrorResponse {
        private final String error;
    }

    /**
     * Exception handler for {@link BadRequestException}.
     * 
     * @param exception
     *            the exception
     * 
     * @return the error response
     */
    @ExceptionHandler
    @ResponseBody
    ResponseEntity<ErrorResponse> handle(BadRequestException exception) {
        log.info("Returning Exception; message = {}", exception.getMessage());

        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(exception.getMessage()),
                exception.getStatus());
    }

}
