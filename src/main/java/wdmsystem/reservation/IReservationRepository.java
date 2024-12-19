package wdmsystem.reservation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.customer.id = :customerId AND r.startTime > :upcomingDate")
    List<Reservation> findReservationByCustomerId(
            @Param("customerId") Integer customerId,
            @Param("upcomingDate") LocalDateTime upcomingDate,
            Pageable pageable
    );

    @Query("SELECT r FROM Reservation r WHERE r.service.id = :serviceId AND r.startTime >= :dayStart AND r.startTime <= :dayEnd")
    List<Reservation> findReservationsByServiceIdAndDate(
            @Param("serviceId") int serviceId,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd
    );

    @Query("SELECT r FROM Reservation r WHERE r.startTime > :upcomingDate")
    List<Reservation> findUpcomingReservations(
            @Param("upcomingDate") LocalDateTime upcomingDate,
            Pageable pageable
    );
}
