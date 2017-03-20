
package de.hrs.innovation.guests.tracking.guest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hrs.innovation.guests.tracking.guest.domain.Guest;
import de.hrs.innovation.guests.tracking.guest.domain.GuestCreateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author Susanna Ferrari
 */
@RestController
@Api
@RequestMapping(path = "/guests", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GuestResource {

    @Autowired
    private GuestService guestService;

    @RequestMapping(method = RequestMethod.POST, path = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Creates a new guest and returns its ID.", response = Integer.class)
    public Integer create(@RequestBody @Validated GuestCreateRequest guest) {
        return guestService.create(guest);
    }

    @RequestMapping(method = RequestMethod.GET, path = "")
    @ApiOperation(value = "Get a list of guests, optionally filtered by name.", response = List.class)
    public List<Guest> getGuests(
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<String> firstName) {
        return guestService.findGuests(lastName, firstName);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/check-in")
    @ApiOperation(value = "Check-in an existing guest.")
    public void checkInGuest(@PathVariable Integer id) {
        guestService.checkIn(id);
    }

    /*
     * Used for testing the App, in a microservice world it has to be implemented, but for this
     * prototype I don't want to get confused with the api-gateway check-out, that checks if the
     * guest still has parcels to pick-up.
     * 
     * @RequestMapping(method = RequestMethod.POST, path = "/{id}/_check_out")
     * 
     * @ApiOperation(value = "Check-out a guest.") public void checkOutGuest(@PathVariable Integer
     * id) { guestService.checkOut(id); }
     */

}
