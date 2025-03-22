package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto createBooking(long userId, BookingInputDto booking);

    BookingDto approveBooking(long userId, long bookingId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    Collection<BookingDto> getAllBookingsByUser(long userId, BookingState state);

    Collection<BookingDto> getAllBookingsByUserItems(long userId, BookingState state);

}