package com.example.wdmsystem.employee.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: Request validation
//TODO: Error response codes
//TODO: Custom response messages
@RestController
public final class EmployeeController {
    private final EmployeeService _employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this._employeeService = employeeService;
    }

    @PostMapping("/employees")
    ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO request) {
        Employee newEmployee = _employeeService.createEmployee(request);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/employees")
    ResponseEntity<List<Employee>> getEmployees(@RequestParam EmployeeType type,
                                                @RequestParam(required = false) int limit) {
        List<Employee> employees = _employeeService.getEmployees(type, limit);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    ResponseEntity<Employee> getEmployee(@PathVariable int employeeId) {
        Employee employee = _employeeService.getEmployee(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/employees/{employeeId}")
    ResponseEntity<Employee> updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeDTO request) {
        _employeeService.updateEmployee(employeeId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/employees/{employeeId}")
    ResponseEntity<Employee> deleteEmployee(@PathVariable int employeeId) {
        _employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
