package wdmsystem.customer.system;


import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.system.IMerchantRepository;
import wdmsystem.merchant.system.Merchant;
import wdmsystem.reservation.system.IReservationRepository;
import wdmsystem.reservation.system.ReservationDTO;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wdmsystem.reservation.system.Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final ICustomerRepository customerRepository;
    private final IReservationRepository reservationRepository;
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public CustomerDTO createCustomer(CustomerDTO createCustomer) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(() ->
                new NotFoundException("Merchant with id " + currentUser.getMerchantId() + " not found"));

        Customer customer = new Customer(
                merchant,
                createCustomer.firstName(),
                createCustomer.lastName(),
                createCustomer.phone(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        customerRepository.save(customer);
        return dtoMapper.Customer_ModelToDTO(customer);
    }

    public List<CustomerDTO> getCustomers(LocalDateTime createdAtMin, LocalDateTime createdAtMax, Integer limit) {
        if (createdAtMin != null && createdAtMax != null && createdAtMin.isAfter(createdAtMax)) {
            throw new InvalidInputException("createdAtMin must be earlier than createdAtMax.");
        }

        LocalDateTime lowerDate = LocalDateTime.of(0,1,1,0,1);
        LocalDateTime upperDate = LocalDateTime.of(9999,12,30,23,59);
        if (createdAtMin != null) {
            lowerDate = createdAtMin;
        }
        if (createdAtMax != null) {
            upperDate = createdAtMax;
        }

        if (limit == null || limit <= 0 || limit > 250) {
            limit = 50;
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        List<Customer> customerList = customerRepository.findCustomersWithinDateRange(lowerDate, upperDate, pageRequest);
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (Customer customer : customerList) {
            customerDTOList.add(dtoMapper.Customer_ModelToDTO(customer));
        }
        return customerDTOList;
    }

    public CustomerDTO getCustomer(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        return dtoMapper.Customer_ModelToDTO(customer);
    }

    public void updateCustomer(int id, CustomerDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (request.firstName() != null) {
            if (request.firstName().length() > 30) {
                throw new InvalidInputException("First name must be less than 30 characters");
            }
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            if (request.lastName().length() > 30) {
                throw new InvalidInputException("Last name must be less than 30 characters");
            }
            customer.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }

        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void deleteCustomer(int id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }

        customerRepository.deleteById(id);
    }

    public List<ReservationDTO> getCustomerReservations(int customerId, boolean upcoming, Integer limit) {
        if (limit == null || limit <= 0 || limit > 250) {
            limit = 50;
        }
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer with ID " + customerId + " does not exist");
        }

        LocalDateTime upcomingDate = LocalDateTime.now();
        if (!upcoming) {
            upcomingDate = LocalDateTime.of(0,1,1,0,1);
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        List<Reservation> reservationList = reservationRepository.findReservationByCustomerId(customerId, upcomingDate, pageRequest);
        List<ReservationDTO> reservationDTOList = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            reservationDTOList.add(dtoMapper.Reservation_ModelToDTO(reservation));
        }
        return reservationDTOList;
    }
}
