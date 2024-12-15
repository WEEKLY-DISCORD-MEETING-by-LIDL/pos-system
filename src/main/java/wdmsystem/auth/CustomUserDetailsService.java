package wdmsystem.auth;

import wdmsystem.employee.system.Employee;
import wdmsystem.employee.system.IEmployeeRepository;
import wdmsystem.exception.NotFoundException;
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
                employee.getMerchant().getId(),
                List.of(new SimpleGrantedAuthority("ROLE_" + employee.getEmployeeType().name()))
        );
    }
}