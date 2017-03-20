
package de.hrs.innovation.guests.tracking.apigateway;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import de.hrs.innovation.guests.tracking.apigateway.domain.GuestInfo;
import de.hrs.innovation.guests.tracking.apigateway.domain.GuestInfo.GuestStatus;
import de.hrs.innovation.guests.tracking.config.BadRequestException;
import de.hrs.innovation.guests.tracking.guest.GuestService;
import de.hrs.innovation.guests.tracking.guest.domain.Guest;
import de.hrs.innovation.guests.tracking.parcel.ParcelService;
import de.hrs.innovation.guests.tracking.parcel.domain.Parcel;

/**
 *
 * @author Susanna Ferrari
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiGatewayServiceTest {

    @Mock
    private GuestService guestService;

    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private ApiGatewayService apiGatewayService;

    private Guest guest;
    private Parcel parcel;

    @Before
    public void setup() {
        guest = new Guest()
                .setFirstName("firstName")
                .setLastName("lastName")
                .setId(1)
                .setDateOfBirth(LocalDate.now())
                .setCheckInDate(LocalDateTime.now())
                .setCheckOutDate(LocalDateTime.now());

        parcel = new Parcel().setCode("code").setGuestId(1).setAcceptedDate(LocalDateTime.now());

    }

    @Test
    public void getGuestInfoCheckedOutGuestHappyPath() {
        when(guestService.findGuests(Optional.of("lastName"), Optional.of("firstName")))
                .thenReturn(Arrays.asList(guest));

        when(parcelService.findParcelsToPickUp(1)).thenReturn(Arrays.asList());
        GuestInfo guestInfo = apiGatewayService.getGuestInfo("lastName", "firstName");

        assertThat(guestInfo.getId(), is(guest.getId()));
        assertThat(guestInfo.getFirstName(), is(guest.getFirstName()));
        assertThat(guestInfo.getLastName(), is(guest.getLastName()));
        assertThat(guestInfo.getDateOfBirth(), is(guest.getDateOfBirth()));

        assertThat(guestInfo.getStatus(), is(GuestStatus.CHECKED_OUT));
        assertNull(guestInfo.getParcelsToPickUp());
    }

    @Test
    public void getGuestInfoCheckedInGuestHappyPath() {
        guest.setCheckOutDate(null);

        when(guestService.findGuests(Optional.of("lastName"), Optional.of("firstName")))
                .thenReturn(Arrays.asList(guest));

        when(parcelService.findParcelsToPickUp(1)).thenReturn(Arrays.asList(parcel));
        GuestInfo guestInfo = apiGatewayService.getGuestInfo("lastName", "firstName");

        assertThat(guestInfo.getStatus(), is(GuestStatus.CHECKED_IN));

        assertThat(guestInfo.getParcelsToPickUp().size(), is(1));
        assertThat(guestInfo.getParcelsToPickUp().get(0).getCode(), is(parcel.getCode()));
        assertThat(
                guestInfo.getParcelsToPickUp().get(0).getAcceptedDate(),
                is(parcel.getAcceptedDate()));
    }

    @Test
    public void whenCheckOutGuestThatHasParcelsToPickUpThenReturnException() {

        when(guestService.findGuests(Optional.of("lastName"), Optional.of("firstName")))
                .thenReturn(Arrays.asList(guest));

        when(parcelService.findParcelsToPickUp(1)).thenReturn(Arrays.asList(parcel));
        try {
            apiGatewayService.checkOutGuest(1);
            fail();
        } catch (BadRequestException ex) {
            assertThat(ex.getStatus(), is(HttpStatus.BAD_REQUEST));
            verifyNoMoreInteractions(guestService);
        }

    }

}
