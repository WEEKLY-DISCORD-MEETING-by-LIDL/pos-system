package com.example.wdmsystem.service.system;


import com.example.wdmsystem.category.system.Category;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public final class ServiceService {
    public ServiceService() {

    }

    public Service createService(ServiceDTO request) {
        return null;
    }

    public List<Service> getServices(Category category, int limit) {
        return null;
    }

    public void updateService(int serviceId, ServiceDTO request) {

    }

    public void deleteService(int serviceId) {

    }

    public Service getService(int serviceId) {
        return null;
    }

    public List<Date> getAvailableTimes(int serviceId, Date date) {
        return null;
    }

}
