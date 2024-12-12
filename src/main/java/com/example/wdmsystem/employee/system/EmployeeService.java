package com.example.wdmsystem.employee.system;

import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(EmployeeDTO request) {

        String hashedPassword = passwordEncoder.encode(request.password());
        Employee employee = new Employee(
                0,
                0, // placeholder
                request.firstName(),
                request.lastName(),
                request.employeeType(),
                request.username(),
                hashedPassword,
                LocalDateTime.now()
        );
        employee.setUpdatedAt(employee.getCreatedAt());

        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees(EmployeeType type, int limit) {

        if (limit <= 0) {
            limit = 50;
        }

        return employeeRepository.getEmployeesByEmployeeType(type, Limit.of(limit));
    }

    public Employee getEmployee(int employeeId) {

        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));
    }

    public void updateEmployee(int employeeId, EmployeeDTO request) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmployeeType(request.employeeType());
        employee.setUsername(request.username());
        String hashedPassword = passwordEncoder.encode(request.password());
        employee.setPassword(hashedPassword);
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);
    }

    public void deleteEmployee(int employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        }
        else {
            throw new NotFoundException("Employee with id " + employeeId + " not found");
        }
    }
}
