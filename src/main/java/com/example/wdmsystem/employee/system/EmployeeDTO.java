package com.example.wdmsystem.employee.system;

/// CreateEmployee is used as a DTO in both creation and updates, so renamed to a more general term
public record EmployeeDTO(String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
