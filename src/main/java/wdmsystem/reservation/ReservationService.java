package wdmsystem.reservation;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import wdmsystem.auth.CustomUserDetails;
import wdmsystem.customer.Customer;
import wdmsystem.customer.ICustomerRepository;
import wdmsystem.employee.Employee;
import wdmsystem.employee.IEmployeeRepository;
import wdmsystem.exception.NotFoundException;
import wdmsystem.payment.Payment;
import wdmsystem.service.IServiceRepository;
import wdmsystem.service.Service;
import wdmsystem.service.ServiceDTO;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        Reservation reservation = new Reservation(
                customer,
                service,
                employee,
                request.startTime(),
                request.endTime(),
                request.reservationStatus() != null ? request.reservationStatus() : ReservationStatus.PENDING, // if null, Default set to CONFIRMED
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

//        if (request.employeeId() != null) {
//            if (!employeeRepository.existsById(request.employeeId())) {
//                throw new NotFoundException("Employee not found");
//            }
//            reservation.setEmployeeId(request.employeeId());
//        }
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

    public List<ReservationDTO> getReservations(Boolean upcoming, Integer limit) {
        if (limit == null || limit <= 0 || limit > 250) {
            limit = 50;
        }

        LocalDateTime upcomingDate = LocalDateTime.now();
        if (upcoming != null && !upcoming) {
            upcomingDate = LocalDateTime.of(0,1,1,0,0);
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        List<Reservation> reservations = reservationRepository.findUpcomingReservations(upcomingDate, pageRequest);

        List<ReservationDTO> dtoList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            dtoList.add(dtoMapper.Reservation_ModelToDTO(reservation));
        }
        return dtoList;
    }

    public BigDecimal getUnpaidPrice(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation with id " + reservationId + " not found"));

        BigDecimal price = reservation.getService().price;

        BigDecimal totalAmountPaid = BigDecimal.valueOf(0);

        for(Payment payment : reservation.getPayments()) {
            totalAmountPaid = totalAmountPaid.add(payment.totalAmount);
        }

        return price.subtract(totalAmountPaid).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public boolean isOwnedByCurrentUser(int reservationId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
                new NotFoundException("Reservation with id " + reservationId + " not found"));

        //TODO is this correct?
        Employee employee = employeeRepository.findById(reservation.getEmployee().id).orElseThrow(() ->
                new NotFoundException("Employee with id " + reservation.getEmployee().id + " not found"));
//        Employee employee = employeeRepository.findById(reservation.getEmployeeId()).orElseThrow(() ->
//                new NotFoundException("Employee with id " + reservation.getEmployeeId() + " not found"));

        return employee.getMerchant().id == currentUser.getMerchantId();
    }

}
