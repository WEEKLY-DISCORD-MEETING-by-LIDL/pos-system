package com.example.wdmsystem.employee.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeService _employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this._employeeService = employeeService;
    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO request) {
        EmployeeDTO newEmployee = _employeeService.createEmployee(request);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<List<EmployeeDTO>> getEmployees(@RequestParam EmployeeType type,
                                                @RequestParam(required = false) Integer limit) {

        List<EmployeeDTO> employees = _employeeService.getEmployees(type, limit);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @employeeService.isOwnedByCurrentUser(#employeeId))")
    ResponseEntity<EmployeeDTO> getEmployee(@PathVariable int employeeId) {
        EmployeeDTO employee = _employeeService.getEmployee(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @employeeService.isOwnedByCurrentUser(#employeeId))")
    ResponseEntity<Employee> updateEmployee(@PathVariable int employeeId, @RequestBody UpdateEmployeeDTO request) {
        _employeeService.updateEmployee(employeeId, request);
        // changed to NO_CONTENT from OK
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @employeeService.isOwnedByCurrentUser(#employeeId))")
    ResponseEntity<Employee> deleteEmployee(@PathVariable int employeeId) {
        _employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
