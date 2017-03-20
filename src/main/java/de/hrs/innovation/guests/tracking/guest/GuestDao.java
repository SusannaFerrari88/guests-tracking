
package de.hrs.innovation.guests.tracking.guest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import de.hrs.innovation.guests.tracking.guest.domain.Guest;

/**
 *
 * @author Susanna Ferrari
 */
@Repository
public class GuestDao {

    // To simplify the solution, this is my Guest DB table
    private final List<Guest> guests = new ArrayList<>();

    /**
     * Store a guest in the DB.
     * 
     * @param guest
     *            the object to store
     */
    public void store(Guest guest) {
        guests.add(guest);
        // To simplify the solution, the id of the entities are simply their
        // position in the list
        guest.setId(guests.indexOf(guest));
    }

    /**
     * Find a guest by last and first name.
     * 
     * @param lastName
     *            the last name
     * @param firstName
     *            the first name
     * @return a filtered list of guests
     */
    public List<Guest> findByName(String lastName, String firstName) {
        return guests
                .stream()
                .filter(
                        guest -> guest.getFirstName().equals(firstName)
                                && guest.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    /**
     * Find all the guests.
     * 
     * @return the complete list of guests
     */
    public List<Guest> findAll() {
        return guests;
    }

    /**
     * Find a guest by id.
     * 
     * @param id
     *            the guest id
     * @return an optional guest
     */
    public Optional<Guest> find(Integer id) {
        if (guests.size() <= id) {
            return Optional.empty();
        }
        return Optional.of(guests.get(id));
    }

}
