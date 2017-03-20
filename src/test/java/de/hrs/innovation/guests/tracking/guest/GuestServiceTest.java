
package de.hrs.innovation.guests.tracking.guest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import de.hrs.innovation.guests.tracking.config.BadRequestException;
import de.hrs.innovation.guests.tracking.guest.GuestDao;
import de.hrs.innovation.guests.tracking.guest.GuestService;
import de.hrs.innovation.guests.tracking.guest.domain.Guest;

/**
 *
 * @author Susanna Ferrari
 */
@RunWith(MockitoJUnitRunner.class)
public class GuestServiceTest {

    @Mock
    private GuestDao dao;

    @InjectMocks
    private GuestService service;

    @Test
    public void whenFindByNameAndNoLastNameWasDefinedThenThrowBadRequestException() {
        try {
            service.findGuests(Optional.empty(), Optional.of("name"));
            fail();
        } catch (BadRequestException ex) {
            verifyZeroInteractions(dao);
            assertThat(ex.getStatus(), is(HttpStatus.BAD_REQUEST));
        }
    }

    @Test
    public void whenFindByIdReturnsEmptyOptionalThenThrowBadRequestException() {
        when(dao.find(1)).thenReturn(Optional.empty());
        try {
            service.findById(1);
            fail();
        } catch (BadRequestException ex) {
            assertThat(ex.getStatus(), is(HttpStatus.BAD_REQUEST));
        }
    }

    @Test
    public void checkOutHappyPath() {
        when(dao.find(1)).thenReturn(Optional.of(new Guest().setCheckInDate(LocalDateTime.now())));
        service.checkOut(1);
    }

}
