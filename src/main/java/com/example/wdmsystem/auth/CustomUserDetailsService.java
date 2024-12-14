package com.example.wdmsystem.auth;

import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.employee.system.IEmployeeRepository;
import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IEmployeeRepository employeeRepository;

    public CustomUserDetailsService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Employee not found: " + username));

        return new CustomUserDetails(
                employee.getUsername(),
                employee.getPassword(),
                employee.getMerchantId(),
                List.of(new SimpleGrantedAuthority("ROLE_" + employee.getEmployeeType().name()))
        );
    }
}