package com.example.wdmsystem.employee.system;

import org.springframework.stereotype.Service;

import java.util.List;

//TODO:Add logic to interact with database
@Service
public class EmployeeService{
    public EmployeeService() {
    }

    public Employee createEmployee(EmployeeDTO request) {
        return null;
    }

    public List<Employee> getEmployees(EmployeeType type, int limit) {
        return null;
    }

    public Employee getEmployee(int employeeId) {
        return null;
    }

    public void updateEmployee(int employeeId, EmployeeDTO request) {
    }

    public void deleteEmployee(int employeeId) {}
}