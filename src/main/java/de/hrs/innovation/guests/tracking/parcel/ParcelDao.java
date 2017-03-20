
package de.hrs.innovation.guests.tracking.parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import de.hrs.innovation.guests.tracking.config.BadRequestException;
import de.hrs.innovation.guests.tracking.parcel.domain.Parcel;

/**
 *
 * @author Susanna Ferrari
 */
@Repository
public class ParcelDao {

    // To simplify the solution, this is my Parcel DB table
    private final List<Parcel> parcels = new ArrayList<>();

    /**
     * Find all parcels.
     * 
     * @return the complete list of parcels
     */
    public List<Parcel> findAll() {
        return parcels;
    }

    /**
     * Store a parcel.
     * 
     * @param parcel
     *            object to store
     */
    public void store(Parcel parcel) {
        if (find(parcel.getCode()).isPresent()) {
            throw new BadRequestException("the parcel code already exists");
        }
        parcels.add(parcel);
    }

    /**
     * Find a parcel by code.
     * 
     * @param code
     *            the parcel code
     * @return an optional parcel
     */
    public Optional<Parcel> find(String code) {
        return parcels //
                .stream()
                .filter(parcel -> parcel.getCode().equals(code))
                .findFirst();
    }

    /**
     * Find parcels by guest Id and that has to be picked up (pickUpDate is null).
     * 
     * @param guestId
     *            the guest id
     * @return a filtered list of parcels
     */
    public List<Parcel> findByGuestIdAndPickUpDateNull(Integer guestId) {
        return parcels //
                .stream()
                .filter(
                        parcel -> parcel.getGuestId().equals(guestId)
                                && parcel.getPickUpDate() == null)
                .collect(Collectors.toList());
    }

}
