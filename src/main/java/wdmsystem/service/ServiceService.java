package wdmsystem.service;


import wdmsystem.auth.CustomUserDetails;
import wdmsystem.category.Category;
import wdmsystem.category.ICategoryRepository;
import wdmsystem.discount.IDiscountRepository;
import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.reservation.IReservationRepository;
import wdmsystem.reservation.Reservation;
import wdmsystem.tax.ITaxRepository;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceService {
    private final IServiceRepository serviceRepository;
    private final IReservationRepository reservationRepository;
    private final IMerchantRepository merchantRepository;
    private final ICategoryRepository categoryRepository;
    private final ITaxRepository taxRepository;
    private final IDiscountRepository discountRepository;
    private final DTOMapper dtoMapper;

    public ServiceDTO createService(ServiceDTO request) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(
                () -> new NotFoundException("Merchant with ID " + currentUser.getMerchantId() + " not found")
        );

        Service service = new Service(
                merchant,
                request.title(),
                null,
                request.price(),
                null,
                null,
                request.durationMins(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        addOptional(service, request);
        serviceRepository.save(service);
        return dtoMapper.Service_ModelToDTO(service);
    }

    private void addOptional(Service service, ServiceDTO request) {
        if (request.taxId() != null) {
            service.tax = taxRepository.findById(request.taxId()).orElse(null);
        }

        if (request.categoryId() != null) {
            service.category = categoryRepository.findById(request.categoryId()).orElse(null);
        }

        if (request.discountId() != null) {
            service.discount = discountRepository.findById(request.discountId()).orElse(null);
        }
    }

    public List<ServiceDTO> getServices(Category category, Integer limit) {
        Integer categoryId = (category != null) ? category.getId() : null;

        if (limit == null || limit < 0 || limit > 250) {
            limit = 50;
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        List<Service> serviceList;
        if(isAdmin){
            serviceList = serviceRepository.findServicesByCategoryId(categoryId, pageRequest);
        }else{
            serviceList = serviceRepository.findServicesByCategoryIdAndMerchantId(categoryId, currentUser.getMerchantId(), pageRequest);
        }

        List<ServiceDTO> dtoList = new ArrayList<>();
        for (Service service : serviceList) {
            dtoList.add(dtoMapper.Service_ModelToDTO(service));
        }
        return dtoList;
    }

    public void updateService(int serviceId, ServiceDTO request) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        if (request.title() != null) {
            if (request.title().length() > 30) {
                throw new InvalidInputException("Title must be less than 30 characters");
            }
            service.setTitle(request.title());
        }
        if (request.categoryId() != null) {
            //TODO check if category exists and set category
//            service.setCategory(request.categoryId());
        }
        if (request.price() != null) {
            service.setPrice(request.price());
        }
        if (request.discountId() != null) {
            //TODO check if discount exists
//            service.setDiscountId(request.discountId());
        }
        if (request.taxId() != null) {
            //TODO check if tax exists
//            service.setTaxId(request.taxId());
        }
        if (request.durationMins() != null) {
            service.setDurationMins(request.durationMins());
        }

        service.setUpdatedAt(LocalDateTime.now());
        serviceRepository.save(service);
    }

    public void deleteService(int serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new NotFoundException("Service not found");
        }

        serviceRepository.deleteById(serviceId);
    }

    public ServiceDTO getService(int serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service not found"));
        return dtoMapper.Service_ModelToDTO(service);
    }

    public List<LocalDateTime> getAvailableTimes(int serviceId, LocalDate date) {
        LocalDateTime dayStart = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
        LocalDateTime dayEnd = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 59, 59);
        List<Reservation> reservations = reservationRepository.findReservationsByServiceIdAndDate(serviceId, dayStart, dayEnd);

        reservations.sort(Comparator.comparing(Reservation::getStartTime));

        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime currentAvailableStart = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        for (Reservation reservation : reservations) {
            LocalDateTime reservationStart = reservation.getStartTime();
            LocalDateTime reservationEnd = reservation.getEndTime();

            if (currentAvailableStart.isBefore(reservationStart)) {
                availableTimes.add(currentAvailableStart);
                availableTimes.add(reservationStart);
            }

            if (currentAvailableStart.isBefore(reservationEnd)) {
                currentAvailableStart = reservationEnd;
            }
        }

        if (currentAvailableStart.isBefore(endTime)) {
            availableTimes.add(currentAvailableStart);
            availableTimes.add(endTime);
        }

        return availableTimes;
    }

    public boolean isOwnedByCurrentUser(int serviceId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Service service = serviceRepository.findById(serviceId).orElseThrow(() ->
                new NotFoundException("Service with id " + serviceId + " not found"));

        return service.getMerchant().id == currentUser.getMerchantId();
    }
}
