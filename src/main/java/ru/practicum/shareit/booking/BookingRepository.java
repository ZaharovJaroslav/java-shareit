package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Modifying
    @Query("UPDATE Booking b "
            + "SET b.status = :status  "
            + "WHERE b.id = :bookingId")
    void save(BookingStatus status, Long bookingId);


    Collection<Booking> findByBookerIdOrderByStartDesc(long id);

    Collection<Booking> findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(long userId,
                                                                                    LocalDateTime end,
                                                                                    LocalDateTime start);

    Collection<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime now);

    Collection<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime now);

    Collection<Booking> findByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(long userId,
                                                                                 LocalDateTime now,
                                                                                 BookingStatus status);

    Collection<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.ownerId = :ownerId "
            + "ORDER BY b.start DESC")
    Collection<Booking> findByItemOwnerId(Long ownerId);


    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.ownerId = :ownerId "
            + "AND :time between b.start AND b.end "
            + "ORDER BY b.start DESC")
    Collection<Booking> findCurrentBookingsOwner(Long ownerId, LocalDateTime time);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.ownerId = :ownerId "
            + "AND b.end < :time "
            + "ORDER BY b.start DESC")
    Collection<Booking> findPastBookingsOwner(Long ownerId, LocalDateTime time);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.ownerId = :ownerId "
            + "AND b.start > :time "
            + "ORDER BY b.start DESC")
    Collection<Booking> findFutureBookingsOwner(long ownerId, LocalDateTime time);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.ownerId = :ownerId "
            + "AND b.start > :time AND b.status = :status "
            + "ORDER BY b.start DESC")
    Collection<Booking> findWaitingBookingsOwner(Long ownerId, LocalDateTime time, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.ownerId = :ownerId "
            + "AND b.status = :status "
            + "ORDER BY b.start DESC")
    Collection<Booking> findRejectedBookingsOwner(long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.id = :itemId "
            + "ORDER BY b.start DESC")
    List<Booking> findBookingsItem(Long itemId);

    List<Booking> findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(Long itemId,
                                                                   Long bookerId,
                                                                   BookingStatus status,
                                                                   LocalDateTime time);
}

