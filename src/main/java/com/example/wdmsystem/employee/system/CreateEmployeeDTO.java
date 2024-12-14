package com.example.wdmsystem.employee.system;

public record CreateEmployeeDTO(String firstName, String lastName, EmployeeType employeeType, String username,
                                String password) {
}
