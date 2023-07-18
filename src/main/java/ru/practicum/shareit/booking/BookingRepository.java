package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
//    @Query(value = "select b from bookings b join items i on b.booker_id = i.owner_id where b.booker_id = ?1 and i.owner_id = ?2", nativeQuery = true)
//    List<Booking> findByIdAndBookerId(Long bookingId, Long bookerId);

    List<Booking> findBookingByIdAndBookerId(Long bookingId, Long bookerId);

//        @Query(value = "select b from bookings b join items i on b.booker_id = i.owner_id where b.booker_id = ?1 and i.owner_id = ?1", nativeQuery = true)
//    List<Booking> findByOwnerId(Long bookerId);

    List<Booking> findBookingByItemOwnerIdOrderByStatusDesc(Long ownerId);

//    @Query(value = "select b from bookings b where b.booker_id = 1 order by status desc", nativeQuery = true)
    List<Booking> findAllByBookerIdOrderByStatusDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStatusDesc(Long bookerId, LocalDateTime now);

    Optional<Booking> findBookingById(Long bookingId);

    Optional<Booking> findById(Long bookingId);

//    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now);

}
