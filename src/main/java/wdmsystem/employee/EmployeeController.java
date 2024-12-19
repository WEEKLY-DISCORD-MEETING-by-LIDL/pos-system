package wdmsystem.employee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class EmployeeController {
    private final EmployeeService _employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this._employeeService = employeeService;
    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO request) {
        log.info("Received request to create employee: {}", request);
        EmployeeDTO newEmployee = _employeeService.createEmployee(request);
        log.info("Employee created successfully with ID: {}", newEmployee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<List<EmployeeDTO>> getEmployees(@RequestParam EmployeeType type,
                                                   @RequestParam(required = false) Integer limit) {
        log.info("Fetching employees of type: {}, with limit: {}", type, limit);
        List<EmployeeDTO> employees = _employeeService.getEmployees(type, limit);
        log.info("Retrieved {} employees of type: {}", employees.size(), type);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @employeeService.isOwnedByCurrentUser(#employeeId))")
    ResponseEntity<EmployeeDTO> getEmployee(@PathVariable int employeeId) {
        log.info("Fetching details for employee with ID: {}", employeeId);
        EmployeeDTO employee = _employeeService.getEmployee(employeeId);
        log.info("Employee details retrieved: {}", employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @employeeService.isOwnedByCurrentUser(#employeeId))")
    ResponseEntity<Employee> updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeDTO request) {
        log.info("Updating employee with ID: {}", employeeId);
        log.debug("Update request details: {}", request);
        _employeeService.updateEmployee(employeeId, request);
        // changed to NO_CONTENT from OK
        log.info("Employee with ID: {} updated successfully", employeeId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @employeeService.isOwnedByCurrentUser(#employeeId))")
    ResponseEntity<Employee> deleteEmployee(@PathVariable int employeeId) {
        log.info("Deleting employee with ID: {}", employeeId);
        _employeeService.deleteEmployee(employeeId);
        log.info("Employee with ID: {} deleted successfully", employeeId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
