package com.example.wdmsystem.reservation.system;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.customerId = :customerId AND " +
            "(:upcomingDate IS NULL OR r.startTime > :upcomingDate) ORDER BY r.createdAt ASC")
    List<Reservation> findReservationByCustomerId(
            @Param("customerId") int customerId,
            @Param("upcomingDate") LocalDateTime upcomingDate,
            Pageable pageable
    );

    @Query("SELECT r FROM Reservation r WHERE r.serviceId = :serviceId AND DATE(r.startTime) = DATE(:date)")
    List<Reservation> findReservationByServiceIdAndDate(
            @Param("serviceId") int serviceId,
            @Param("date") LocalDateTime date
    );

    @Query("SELECT r FROM Reservation r WHERE r.customerId = :customerId")
    List<Reservation> findReservationsByCustomerId(
            @Param("customerId") int customerId
    );
}
