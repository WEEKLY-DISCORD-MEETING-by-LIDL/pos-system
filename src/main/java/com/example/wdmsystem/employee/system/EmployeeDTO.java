package com.example.wdmsystem.employee.system;

public record EmployeeDTO(String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
