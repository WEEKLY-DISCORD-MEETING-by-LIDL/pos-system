package com.example.wdmsystem.employee.system.authentication;

import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.employee.system.IEmployeeRepository;
import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IEmployeeRepository employeeRepository;

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