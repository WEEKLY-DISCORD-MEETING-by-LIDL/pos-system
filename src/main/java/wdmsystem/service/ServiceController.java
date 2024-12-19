package wdmsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import wdmsystem.category.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class ServiceController {
    private final ServiceService _serviceService;

    public ServiceController(ServiceService serviceService) {
        this._serviceService = serviceService;
    }

    @PostMapping("/services")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO request) {
        log.info("Received request to create service: {}", request);
        ServiceDTO newService = _serviceService.createService(request);
        log.info("Service created successfully: {}", newService);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @GetMapping("/services")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<ServiceDTO>> getServices(@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Integer limit) {
        log.info("Fetching services with category: {}", categoryId);
        List<ServiceDTO> serviceList = _serviceService.getServices(categoryId, limit);
        log.info("Fetched {} services", serviceList.size());
        return new ResponseEntity<>(serviceList, HttpStatus.OK);
    }

    @PutMapping("/services/{serviceId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @serviceService.isOwnedByCurrentUser(#serviceId))")
    ResponseEntity<Service> updateService(@PathVariable int serviceId, @RequestBody ServiceDTO request) {
        log.info("Updating service with ID: {}", serviceId);
        _serviceService.updateService(serviceId, request);
        log.info("Service with ID: {} updated successfully", serviceId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/services/{serviceId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @serviceService.isOwnedByCurrentUser(#serviceId))")
    ResponseEntity<Service> deleteService(@PathVariable int serviceId) {
        log.info("Deleting service with ID: {}", serviceId);
        _serviceService.deleteService(serviceId);
        log.info("Service with ID: {} deleted successfully", serviceId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/services/{serviceId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @serviceService.isOwnedByCurrentUser(#serviceId))")
    ResponseEntity<ServiceDTO> getService(@PathVariable int serviceId) {
        log.info("Fetching service with ID: {}", serviceId);
        ServiceDTO service = _serviceService.getService(serviceId);
        log.info("Fetched service: {}", service);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    @GetMapping("/services/{serviceId}/available-times")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @serviceService.isOwnedByCurrentUser(#serviceId))")
    ResponseEntity<List<LocalDateTime>> getAvailableTimes(@PathVariable int serviceId, @RequestParam LocalDate date) {
        log.info("Fetching available times for service ID: {} on date: {}", serviceId, date);
        List<LocalDateTime> dateList = _serviceService.getAvailableTimes(serviceId, date);
        log.info("Fetched {} available times for service ID: {}", dateList.size(), serviceId);
        return new ResponseEntity<>(dateList, HttpStatus.OK);
    }


}
