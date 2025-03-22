package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    @Query("select b from Booking as b " +
            "where b.booker.id = ?1 " +
            "and ?2 > b.start " +
            "and ?2 < b.end")
    Collection<Booking> findAllBookingsCurrent(long bookerId, LocalDateTime current);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    Collection<Booking> findAllByItemIdInOrderByStartDesc(Collection<Long> ids);

    @Query("select b from Booking as b " +
            "where b.booker.id in ?1 " +
            "and ?2 > b.start " +
            "and ?2 < b.end")
    Collection<Booking> findAllBookingsCurrent(Collection<Long> bookerId, LocalDateTime current);

    Collection<Booking> findAllByItemIdInAndEndBeforeOrderByStartDesc(Collection<Long> ids,
                                                                      LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartDesc(Collection<Long> ids,
                                                                       LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndStatusOrderByStartDesc(Collection<Long> ids, BookingStatus status);

    Booking findByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(long userId, long itemId, LocalDateTime current);

    Collection<Booking> findAllByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);

    Collection<Booking> findAllByItemIdAndEndBeforeOrderByEndAsc(long itemId, LocalDateTime time);
}