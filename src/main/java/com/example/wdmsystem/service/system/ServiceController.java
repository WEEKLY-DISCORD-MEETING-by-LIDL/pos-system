package com.example.wdmsystem.service.system;

import com.example.wdmsystem.category.system.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public final class ServiceController {
    private final ServiceService _serviceService;

    public ServiceController(ServiceService serviceService) {
        this._serviceService = serviceService;
    }

    @PostMapping("/services")
    ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO request) {
        ServiceDTO newService = _serviceService.createService(request);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @GetMapping("/services")
    ResponseEntity<List<ServiceDTO>> getServices(@RequestParam(required = false) Category category, @RequestParam(required = false) Integer limit) {
        List<ServiceDTO> serviceList = _serviceService.getServices(category, limit);
        return new ResponseEntity<>(serviceList, HttpStatus.OK);
    }

    @PutMapping("/services/{serviceId}")
    ResponseEntity<Service> updateService(@PathVariable int serviceId, @RequestBody ServiceDTO request) {
        _serviceService.updateService(serviceId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/services/{serviceId}")
    ResponseEntity<Service> deleteService(@PathVariable int serviceId) {
        _serviceService.deleteService(serviceId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/services/{serviceId}")
    ResponseEntity<ServiceDTO> getService(@PathVariable int serviceId) {
        ServiceDTO service = _serviceService.getService(serviceId);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    @GetMapping("/services/{serviceId}/available-times")
    ResponseEntity<List<LocalDateTime>> getAvailableTimes(@PathVariable int serviceId, @RequestParam LocalDate date) {
        List<LocalDateTime> dateList = _serviceService.getAvailableTimes(serviceId, date);
        return  new ResponseEntity<>(dateList, HttpStatus.OK);
    }


}
