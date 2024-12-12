package com.example.wdmsystem.employee.system.authentication;

import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.employee.system.IEmployeeRepository;
import com.example.wdmsystem.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UnauthorizedException {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        return new org.springframework.security.core.userdetails.User(
                employee.getUsername(),
                employee.getPassword(),
                List.of(new SimpleGrantedAuthority(employee.getEmployeeType().name()))
        );
    }
}