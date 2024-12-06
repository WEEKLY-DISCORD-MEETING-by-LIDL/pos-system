package com.example.wdmsystem.service.system;


import com.example.wdmsystem.category.system.Category;
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
        PageRequest pageRequest = PageRequest.of(0, limit);
        return serviceRepository.findServicesByCategoryId(category.id, pageRequest);
    }

    @Transactional
    public void updateService(int serviceId, ServiceDTO request) {
        Optional<Service> service = serviceRepository.findById(serviceId);
        if (service.isEmpty()) {
            throw new NotFoundException("Service not found");
        }
        Service existingService = service.get();

        if (request.merchantId() != null) {
            existingService.setMerchantId(request.merchantId());
        }
        if (request.title() != null) {
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
        serviceRepository.deleteById(serviceId);
    }

    public Service getService(int serviceId) {
        Optional<Service> service = serviceRepository.findById(serviceId);
        return service.orElse(null);
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
