
package de.hrs.innovation.guests.tracking.guest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import de.hrs.innovation.guests.tracking.config.BadRequestException;
import de.hrs.innovation.guests.tracking.guest.domain.Guest;
import de.hrs.innovation.guests.tracking.guest.domain.GuestCreateRequest;

/**
 *
 * @author Susanna Ferrari
 */
@Slf4j
@Service
public class GuestService {

    @Autowired
    private GuestDao dao;

    /**
     * Create a guest and automatically set a check-in date, if the check-in flag is set.
     * 
     * @param createGuestRequest
     *            a request containing the guest info
     * @return the id of the created guest
     */
    public Integer create(GuestCreateRequest createGuestRequest) {
        Guest guest = toGuest(createGuestRequest);
        dao.store(guest);
        log.info("New guest created : {}", guest);
        return guest.getId();
    }

    /**
     * Find guests, optionally filtered by name.
     * 
     * @param lastName
     *            the last name
     * @param firstName
     *            the first name
     * @return a complete or filtered list of guests
     */
    public List<Guest> findGuests(Optional<String> lastName, Optional<String> firstName) {
        if (lastName.isPresent() && firstName.isPresent()) {
            return dao.findByName(lastName.get(), firstName.get());
        }
        if (!lastName.isPresent() && !firstName.isPresent()) {
            return dao.findAll();
        }
        throw new BadRequestException(
                "you must specify both lastName and firstName or none of them");
    }

    /**
     * Check-in a guest.
     * 
     * @param id
     *            the guest id
     */
    public void checkIn(Integer id) {
        Guest guest = findById(id);

        if (guest.getCheckInDate() != null) {
            throw new BadRequestException("the user has already checked-in!");
        }
        guest.setCheckInDate(LocalDateTime.now());
        log.info("Guest {} checked-in.", guest.getId());
    }

    /**
     * Check-out a guest.
     * 
     * @param id
     *            the guest id
     */
    public void checkOut(Integer id) {
        Guest guest = findById(id);

        if (guest.getCheckInDate() == null) {
            throw new BadRequestException("the guest didn't check-in yet!");
        }
        if (guest.getCheckOutDate() != null) {
            throw new BadRequestException("the guest has already checked-out!");
        }
        guest.setCheckOutDate(LocalDateTime.now());
        log.info("Guest {} checked-out.", guest.getId());
    }

    /**
     * Find a guest by id.
     * 
     * @param id
     *            the guest id
     * @return the guest, if present, otherwise it throws an exception
     */
    public Guest findById(Integer id) {
        Optional<Guest> guest = dao.find(id);
        if (!guest.isPresent()) {
            throw new BadRequestException("guest not found");
        }
        return guest.get();
    }

    private Guest toGuest(GuestCreateRequest createGuest) {
        Guest guest = new Guest()
                .setFirstName(createGuest.getFirstName())
                .setLastName(createGuest.getLastName())
                .setDateOfBirth(createGuest.getDateOfBirth());

        if (createGuest.isCheckIn()) {
            guest.setCheckInDate(LocalDateTime.now());
        }
        return guest;
    }

}
