package com.example.wdmsystem.employee.system;

import com.example.wdmsystem.auth.CustomUserDetails;
import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.wdmsystem.utility.DTOMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;
    private final DTOMapper dtoMapper;

    public EmployeeService(IEmployeeRepository employeeRepository, DTOMapper dtoMapper) {
        this.employeeRepository = employeeRepository;
        this.dtoMapper = dtoMapper;
    }

    public Employee createEmployee(CreateEmployeeDTO request) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Employee employee = dtoMapper.CreateEmployee_DTOToModel(request);
        employee.setMerchantId(currentUser.getMerchantId());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedAt(employee.getCreatedAt());

        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees(EmployeeType type, Integer limit) {

        if (limit == null || limit <= 0) {
            limit = 50;
        }

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        System.out.println(isAdmin);
        if(isAdmin) {
            return employeeRepository.getEmployeesByEmployeeType(type, Limit.of(limit));
        } else {
            return employeeRepository.getEmployeesByEmployeeTypeAndMerchantId(type, currentUser.getMerchantId(), Limit.of(limit));
        }
    }

    public Employee getEmployee(int employeeId) {

        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));
    }

    public void updateEmployee(int employeeId, UpdateEmployeeDTO request) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));


        employee = dtoMapper.UpdateEmployee_DTOToModel(request, employee);
        employee.setUpdatedAt(LocalDateTime.now());

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(isAdmin) {
            employee.setMerchantId(request.merchantId());
        }
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

    //Not defined in yaml, but added for auth. Owners can only edit their own employees.
    public boolean isOwnedByCurrentUser(int employeeId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Employee employee = getEmployee(employeeId);
        return employee.getMerchantId() == currentUser.getMerchantId();
    }
}
