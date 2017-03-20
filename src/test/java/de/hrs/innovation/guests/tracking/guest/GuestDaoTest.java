
package de.hrs.innovation.guests.tracking.guest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.hrs.innovation.guests.tracking.guest.GuestDao;
import de.hrs.innovation.guests.tracking.guest.domain.Guest;

/**
 *
 * @author Susanna Ferrari
 */
public class GuestDaoTest {

    private final GuestDao dao = new GuestDao();

    private Guest guest1;
    private Guest guest2;

    @Before
    public void setup() {
        guest1 = new Guest().setLastName("ferrari").setFirstName("susanna");
        guest2 = new Guest().setLastName("pallo").setFirstName("pinco");
        dao.store(guest1);
        dao.store(guest2);
    }

    @Test
    public void whenGuestIsCreatedThenIdIsSet() {
        Guest guest = new Guest();
        dao.store(guest);
        assertThat(guest.getId(), is(2));
    }

    @Test
    public void whenFindByNameThenReturnFilteredList() {
        assertThat(dao.findByName("ferrari", "susanna").size(), is(1));
        assertThat(dao.findByName("ferrari", "susanna").get(0), is(guest1));
    }

    @Test
    public void findByIdHappyPath() {
        assertThat(dao.find(0), is(Optional.of(guest1)));
    }

}
