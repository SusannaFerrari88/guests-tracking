
package de.hrs.innovation.guests.tracking.parcel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hrs.innovation.guests.tracking.parcel.domain.Parcel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author Susanna Ferrari
 */
@RestController
@Api
@RequestMapping(path = "/parcels", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ParcelResource {

    @Autowired
    private ParcelService parcelService;

    /*
     * Used for testing the App, in a microservice world it has to be implemented, but for this
     * prototype I don't want to get confused with the api-gateway accept parcels, that checks if
     * the guest is still checked-in.
     * 
     * @RequestMapping(method = RequestMethod.POST, path = "/_accept", consumes =
     * MediaType.APPLICATION_JSON_UTF8_VALUE)
     * 
     * @ApiOperation(value = "Accepts a parcel and store its info.") public void
     * acceptParcel(@RequestBody ParcelAcceptRequest parcel) { parcelService.accept(parcel); }
     */

    @RequestMapping(method = RequestMethod.POST, path = "/pick-up")
    @ApiOperation(value = "Pick-up all parcels of a single guest.")
    public void pickUpParcel(@RequestParam Integer guestId) {
        parcelService.pickUpAll(guestId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "")
    @ApiOperation(value = "Get a list of all parcels.", response = List.class)
    public List<Parcel> getAllParcels() {
        return parcelService.findAllParcels();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/to-pick-up")
    @ApiOperation(value = "Get a list of parcels to pick-up by guest id.", response = List.class)
    public List<Parcel> findParcelsToPickUp(@RequestParam Integer guestId) {
        return parcelService.findParcelsToPickUp(guestId);
    }

}
