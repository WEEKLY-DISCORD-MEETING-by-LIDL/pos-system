package wdmsystem.reservation;

import wdmsystem.customer.Customer;
import wdmsystem.customer.ICustomerRepository;
import wdmsystem.employee.IEmployeeRepository;
import wdmsystem.exception.NotFoundException;
import wdmsystem.service.IServiceRepository;
import wdmsystem.service.Service;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ReservationService {
    private final IReservationRepository reservationRepository;
    private final ICustomerRepository customerRepository;
    private final IServiceRepository serviceRepository;
    private final IEmployeeRepository employeeRepository;
    private final DTOMapper dtoMapper;

    public ReservationDTO createReservation(ReservationDTO request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Service service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found"));

        if (!employeeRepository.existsById(request.employeeId())) {
            throw new NotFoundException("Employee not found");
        }

        Reservation reservation = new Reservation(
                customer,
                service,
                request.employeeId(), //probably change to employee
                request.startTime(),
                request.endTime(),
                request.reservationStatus() != null ? request.reservationStatus() : ReservationStatus.CONFIRMED, // if null, Default set to CONFIRMED
                request.sendConfirmation(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        reservationRepository.save(reservation);
        return dtoMapper.Reservation_ModelToDTO(reservation);
    }

    public ReservationDTO getReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));
        return dtoMapper.Reservation_ModelToDTO(reservation);
    }

    public void updateReservation(int reservationId, ReservationDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        if (request.employeeId() != null) {
            if (!employeeRepository.existsById(request.employeeId())) {
                throw new NotFoundException("Employee not found");
            }
            reservation.setEmployeeId(request.employeeId());
        }
        if (request.startTime() != null) {
            reservation.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            reservation.setEndTime(request.endTime());
        }
        if (request.reservationStatus() != null) {
            reservation.setReservationStatus(request.reservationStatus());
        }

        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    public void cancelReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        reservation.setReservationStatus(ReservationStatus.CANCELED);
        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    public void deleteReservation(int reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new NotFoundException("Reservation not found");
        }

        reservationRepository.deleteById(reservationId);
    }

}
