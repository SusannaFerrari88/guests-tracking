
package de.hrs.innovation.guests.tracking.parcel;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import de.hrs.innovation.guests.tracking.config.BadRequestException;
import de.hrs.innovation.guests.tracking.parcel.domain.Parcel;
import de.hrs.innovation.guests.tracking.parcel.domain.ParcelAcceptRequest;

/**
 *
 * @author Susanna Ferrari
 */
@Slf4j
@Service
public class ParcelService {

    @Autowired
    private ParcelDao dao;

    /**
     * Find parcels to be picked up by a specific guest.
     * 
     * @param guestId
     *            the guest id
     * @return the list of parcels to pick up
     */
    public List<Parcel> findParcelsToPickUp(Integer guestId) {
        return dao.findByGuestIdAndPickUpDateNull(guestId);
    }

    /**
     * Find all parcels.
     * 
     * @return a complete list of parcels
     */
    public List<Parcel> findAllParcels() {
        return dao.findAll();
    }

    /**
     * Accept a parcel and save its data.
     * 
     * @param parcelAcceptRequest
     *            the request, containing the parcel code and guest Id
     */
    public void accept(ParcelAcceptRequest parcelAcceptRequest) {
        Parcel parcel = new Parcel()
                .setCode(parcelAcceptRequest.getCode())
                .setGuestId(parcelAcceptRequest.getGuestId())
                .setAcceptedDate(LocalDateTime.now());
        dao.store(parcel);
        log.info(
                "Accepted parcel with code: {} for guest with Id: {}",
                parcel.getCode(),
                parcel.getGuestId());
    }

    /**
     * Pick-up all parcels that belong to a specific guest.
     * 
     * @param guestId
     *            the guest id
     */
    public void pickUpAll(Integer guestId) {
        List<Parcel> parcels = dao.findByGuestIdAndPickUpDateNull(guestId);
        if (parcels.isEmpty()) {
            throw new BadRequestException("no parcel was found");
        }
        parcels.forEach(parcel -> parcel.setPickUpDate(LocalDateTime.now()));
        log.info(
                "The parcels of guest with id: {} were picked up. Number or parcels : {}",
                guestId,
                parcels.size());
    }
}
