package com.example.wdmsystem.service.system;


import com.example.wdmsystem.category.system.Category;
import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.reservation.system.IReservationRepository;
import com.example.wdmsystem.reservation.system.Reservation;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@org.springframework.stereotype.Service
public class ServiceService {
    private final IServiceRepository serviceRepository;
    private final IReservationRepository reservationRepository;

    public ServiceService(IServiceRepository serviceRepository, IReservationRepository reservationRepository) {
        this.serviceRepository = serviceRepository;
        this.reservationRepository = reservationRepository;
    }

    public Service createService(ServiceDTO request) {
        Service service = new Service();
        service.setMerchantId(request.merchantId());
        service.setTitle(request.title());
        service.setCategoryId(request.categoryId());
        service.setPrice(request.price());
        service.setDiscountId(request.discountId());
        service.setTaxId(request.taxId());
        service.setDurationMins(request.durationMins());
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(null);

        return serviceRepository.save(service);
    }

    public List<Service> getServices(Category category, int limit) {
//        if (limit <= 0) {
//            throw new InvalidInputException("limit must be greater than 0. Current limit: " + limit);
//        }
        if (limit < 0 || limit > 250) {
            limit = 50;
        }
        PageRequest pageRequest = PageRequest.of(0, limit);
        return serviceRepository.findServicesByCategoryId(category.id, pageRequest);
    }

    @Transactional
    public void updateService(int serviceId, ServiceDTO request) {
        if (serviceId < 0) {
            throw new InvalidInputException("Service must be greater than 0");
        }

        Optional<Service> service = serviceRepository.findById(serviceId);

        if (service.isEmpty()) {
            throw new NotFoundException("Service not found");
        }

        Service existingService = service.get();

        if (request.title() != null) {
            if (request.title().trim().isEmpty()) {
                throw new InvalidInputException("Title cannot be empty");
            }
            if (request.title().length() > 30) {
                throw new InvalidInputException("Title must be shorter than 30 characters");
            }
            existingService.setTitle(request.title());
        }
        if (request.categoryId() != null) {
            existingService.setCategoryId(request.categoryId());
        }
        if (request.price() != null) {
            existingService.setPrice(request.price());
        }
        if (request.discountId() != null) {
            existingService.setDiscountId(request.discountId());
        }
        if (request.taxId() != null) {
            existingService.setTaxId(request.taxId());
        }
        if (request.durationMins() != null) {
            existingService.setDurationMins(request.durationMins());
        }

        existingService.setUpdatedAt(LocalDateTime.now());
        serviceRepository.save(existingService);
    }

    public void deleteService(int serviceId) {
        if (serviceId < 0) {
            throw new InvalidInputException("Service id must be greater than 0");
        }
        Optional<Service> service = serviceRepository.findById(serviceId);
        if (service.isEmpty()) {
            throw new NotFoundException("Service not found");
        }
        Service existingService = service.get();
        serviceRepository.delete(existingService);
    }

    public Service getService(int serviceId) {
        Optional<Service> service = serviceRepository.findById(serviceId);
        if (service.isEmpty()) {
            throw new NotFoundException("Service not found");
        }
        else {
            return service.get();
        }
    }

    public List<LocalDateTime> getAvailableTimes(int serviceId, LocalDateTime date) {
        List<Reservation> reservations = reservationRepository.findReservationByServiceIdAndDate(serviceId, date);

        reservations.sort(Comparator.comparing(Reservation::getStartTime));

        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime currentAvailableStart = LocalDateTime.of(date.toLocalDate(), LocalTime.of(0, 0));
        LocalDateTime endTime = LocalDateTime.of(date.toLocalDate(), LocalTime.of(23, 59));

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

}
