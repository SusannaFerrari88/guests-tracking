
package de.hrs.innovation.guests.tracking.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hrs.innovation.guests.tracking.apigateway.domain.GuestInfo;
import de.hrs.innovation.guests.tracking.parcel.domain.ParcelAcceptRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author Susanna Ferrari
 */
@RestController
@Api
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiGatewayResource {

    @Autowired
    private ApiGatewayService apiGatewayService;

    @RequestMapping(method = RequestMethod.GET, path = "/get-guest-info")
    @ApiOperation(value = "Gets a guest info, including the list of parcels to pick-up.", response = GuestInfo.class)
    public GuestInfo getGuestInfo(@RequestParam String lastName, @RequestParam String firstName) {
        return apiGatewayService.getGuestInfo(lastName, firstName);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/accept-parcel", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Accepts a parcel and store its info.")
    public void acceptParcel(@RequestBody ParcelAcceptRequest parcelAcceptRequest) {
        apiGatewayService.acceptParcel(parcelAcceptRequest);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/check-out-guest")
    @ApiOperation(value = "Check-out a guest.")
    public void checkOutGuest(@RequestParam Integer guestId) {
        apiGatewayService.checkOutGuest(guestId);
    }

}
