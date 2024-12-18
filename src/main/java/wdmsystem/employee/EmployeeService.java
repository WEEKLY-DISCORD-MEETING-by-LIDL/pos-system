package wdmsystem.employee;

import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;

import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;
    private final IMerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final DTOMapper dtoMapper;


    public EmployeeDTO createEmployee(EmployeeDTO request) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String hashedPassword = passwordEncoder.encode(request.password());

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(() ->
                    new NotFoundException("Merchant with id " + currentUser.getMerchantId() + " not found"));

        Employee employee = new Employee(
                0,
                merchant,
                request.firstName(),
                request.lastName(),
                request.employeeType(),
                request.username(),
                hashedPassword,
                LocalDateTime.now()
        );
        employee.setUpdatedAt(employee.getCreatedAt());
        employeeRepository.save(employee);
        return dtoMapper.Employee_ModelToDTO(employee);
    }

    public List<EmployeeDTO> getEmployees(EmployeeType type, Integer limit) {

        if (limit == null || limit <= 0) {
            limit = 50;
        }

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        System.out.println(isAdmin);
        List<Employee> employeeList;
         if(isAdmin) {
             employeeList =  employeeRepository.getEmployeesByEmployeeType(type, Limit.of(limit));
        } else {
             employeeList = employeeRepository.getEmployeesByEmployeeTypeAndMerchantId(type, currentUser.getMerchantId(), Limit.of(limit));
        }
         List<EmployeeDTO> dtoList = new ArrayList<>();
         for (Employee employee : employeeList) {
             dtoList.add(dtoMapper.Employee_ModelToDTO(employee));
         }
         return dtoList;
    }

    public EmployeeDTO getEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));
        return dtoMapper.Employee_ModelToDTO(employee);
    }

    public void updateEmployee(int employeeId, UpdateEmployeeDTO request) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));

        dtoMapper.UpdateEmployee_DTOToModel(employee, request);
        employee.setUpdatedAt(LocalDateTime.now());

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(isAdmin && request.merchantId() != null) {
            Merchant merchant = merchantRepository.findById(request.merchantId()).orElseThrow(() ->
                    new NotFoundException("Merchant with id " + request.merchantId() + " not found"));
            employee.setMerchant(merchant);
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

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException("Employee with id " + employeeId + " not found"));
        return employee.getMerchant().id == currentUser.getMerchantId();
    }
}
