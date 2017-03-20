
package de.hrs.innovation.guests.tracking.apigateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hrs.innovation.guests.tracking.apigateway.domain.GuestInfo;
import de.hrs.innovation.guests.tracking.apigateway.domain.GuestInfo.GuestStatus;
import de.hrs.innovation.guests.tracking.apigateway.domain.ParcelInfo;
import de.hrs.innovation.guests.tracking.config.BadRequestException;
import de.hrs.innovation.guests.tracking.guest.GuestService;
import de.hrs.innovation.guests.tracking.guest.domain.Guest;
import de.hrs.innovation.guests.tracking.parcel.ParcelService;
import de.hrs.innovation.guests.tracking.parcel.domain.Parcel;
import de.hrs.innovation.guests.tracking.parcel.domain.ParcelAcceptRequest;

/**
 *
 * @author Susanna Ferrari
 */
@Service
public class ApiGatewayService {

    // In a microservice world, here I would inject the http clients that call the Parcel Service
    // and the Guest Service (the client library should be defined inside of each single project).
    // To simplify my solution, I just re-use the Service classes I've created for the other
    // endpoints.

    @Autowired
    private GuestService guestService;

    @Autowired
    private ParcelService parcelService;

    /**
     * Get the full information about a Guest: his personal data, plus a list of parcels to pick up.
     * 
     * @param lastName
     *            the last name of the guest
     * @param firstName
     *            the first name of the guest
     * @return a {@link GuestInfo} object
     */
    public GuestInfo getGuestInfo(String lastName, String firstName) {
        List<Guest> guests = guestService.findGuests(Optional.of(lastName), Optional.of(firstName));
        if (guests.isEmpty()) {
            throw new BadRequestException("no guest found with this name");
        }

        // this prototype is not ready to handle multiple guests with the same name (possible
        // solution: return a list of GuestInfo and ask the user to select the correct one OR
        // have a search by id -unique-)
        Guest guest = guests.get(0);

        GuestInfo guestInfo = toGuestInfo(guest);

        List<Parcel> parcelsToPickUp = parcelService.findParcelsToPickUp(guest.getId());

        if (!parcelsToPickUp.isEmpty()) {
            guestInfo.setParcelsToPickUp(toParcelInfoList(parcelsToPickUp));
        }

        return guestInfo;
    }

    /**
     * Accept a parcel for a guest, only if the guest is currently checked-in.
     * 
     * @param parcelAcceptRequest
     *            a parcel request
     */
    public void acceptParcel(ParcelAcceptRequest parcelAcceptRequest) {
        Guest guest = guestService.findById(parcelAcceptRequest.getGuestId());

        if (guest.getCheckInDate() == null) {
            throw new BadRequestException(
                    "cannot accept this parcel : the user didn't check-in yet");
        }
        if (guest.getCheckOutDate() != null) {
            throw new BadRequestException(
                    "cannot accept this parcel : the user already checked-out");
        }

        parcelService.accept(parcelAcceptRequest);
    }

    /**
     * Check-out a guest, only if he doesn't have parcels to pick-up.
     * 
     * @param id
     *            the guest id
     */
    public void checkOutGuest(Integer id) {
        List<Parcel> parcelsToPickUp = parcelService.findParcelsToPickUp(id);

        if (!parcelsToPickUp.isEmpty()) {
            throw new BadRequestException(
                    "cannot check-out this guest : he didn't pick-up all of his parcels");
        }

        guestService.checkOut(id);
    }

    private List<ParcelInfo> toParcelInfoList(List<Parcel> parcelsToPickUp) {
        return parcelsToPickUp //
                .stream()
                .map(parcel -> toParcelInfo(parcel))
                .collect(Collectors.toList());
    }

    private ParcelInfo toParcelInfo(Parcel parcel) {
        return new ParcelInfo() //
                .setCode(parcel.getCode())
                .setAcceptedDate(parcel.getAcceptedDate());
    }

    private GuestInfo toGuestInfo(Guest guest) {
        return new GuestInfo()
                .setId(guest.getId())
                .setLastName(guest.getLastName())
                .setFirstName(guest.getFirstName())
                .setDateOfBirth(guest.getDateOfBirth())
                .setStatus(toGuestStatus(guest));
    }

    private GuestStatus toGuestStatus(Guest guest) {
        if (guest.getCheckInDate() == null) {
            return GuestStatus.REGISTERED;
        }
        if (guest.getCheckOutDate() == null) {
            return GuestStatus.CHECKED_IN;
        }
        return GuestStatus.CHECKED_OUT;
    }

}
